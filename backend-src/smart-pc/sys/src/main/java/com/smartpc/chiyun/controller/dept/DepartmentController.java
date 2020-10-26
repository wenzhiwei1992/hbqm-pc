package com.smartpc.chiyun.controller.dept;

import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.dao.dept.DepartmentDao;
import com.smartpc.chiyun.service.dept.DepartmentService;
import com.smartpc.chiyun.aop.Log;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.exception.DataNotFoundException;

import com.smartpc.chiyun.model.dept.Department;

import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.model.user.vo.ProductTypeHandleVO;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.vo.ResultVO;

import com.smartpc.chiyun.model.user.vo.dept.DeptVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
@Slf4j
public class DepartmentController {

    @Autowired
    DepartmentService deptService;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    UserDao userDao;

    /**
     * 分页获取班组信息列表
     *
     * @param pageInfo
     * @return
     */
    @Log("查询部门列表")
    @RequestMapping(value = "/basicdata/department/list", method = RequestMethod.POST)
    public ResultVO list(@RequestBody PageInfo pageInfo) {
        if (pageInfo.getPageSize() <= 0 || pageInfo.getCurrentPage() <= 0) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ResultEnum.PAGEINFODATAERR.getMsg(), "");
        }
        Page<Department> departments = deptService.pageableList(pageInfo);

        return ResultVOUtils.success(departments);
    }

    /**
     * 获取部门列表(不分页)
     *
     * @return
     */
    @RequestMapping(value = "/basicdata/department/listAll", method = RequestMethod.GET)
    public ResultVO listAll() {
        // List<Department> departments = deptService.findAll();
        //2019年8月14日14:02:58 李强  修改获取全部部门信息为获取自己所属组织的部门信息
        List<Department> departments = deptService.findByOrgId();
        ResultVO result = ResultVOUtils.success(departments);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getDetails();
        result.setMsg(userDao.findById(user.getId()).get().getDeptId()+"");
        if(!StringUtil.isNotNullAndEmpty(result.getMsg()) && !departments.isEmpty() ){
            result.setMsg(departments.get(0).getId()+"");
        }
        return result;
    }


    @GetMapping(value = "/basicdata/department/id/{id}")
    public ResultVO getDepartmentById(@PathVariable Long id) {
        ResultVO resultVO = new ResultVO();
        Optional<Department> op = deptService.getOne(id);
        op.ifPresent(new Consumer<Department>() {
            @Override
            public void accept(Department department) {
                if (department == null) {
                    resultVO.setStatus(ResultEnum.GETONEFAILED.getStatus());
                    resultVO.setMsg(ResultEnum.GETONEFAILED.getMsg());
                }
                resultVO.setStatus("success");
                resultVO.setEntity(department);
            }
        });
        return resultVO;
    }
    @Log("删除部门")
    @RequestMapping(value = "/basicdata/department/{id}", method = RequestMethod.DELETE)
    public ResultVO delete(@PathVariable Long id) {
        try {
            deptService.deleteDeptById(id);
        } catch (DataNotFoundException e) {
            return  ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(),e.getMsg());
            //return ResultVOUtils.error(ResultEnum.CONSTRAINT.getStatus(), ResultEnum.CONSTRAINT.getMsg());
        }
        return ResultVOUtils.success();
    }
    @Log("删除部门")
    @RequestMapping(value = "/basicdata/department/deletes", method = RequestMethod.DELETE)
    public ResultVO deleteByIds(String ids) {
        String[] split = ids.split(",");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        deptService.deleteDeptsWithIds(list);
        return ResultVOUtils.success();
    }
    @Log("更新部门")
    @RequestMapping(value = "/basicdata/department/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody Department department) {
        if (department.getId() == null || String.valueOf(department.getId()).isEmpty()) {
            return ResultVOUtils.error(ResultEnum.UPDATEMUSTHAVEID.getStatus(), ResultEnum.UPDATEMUSTHAVEID.getMsg());
        }
        //判断该code是否已经存在
        Department exitsInfo = departmentDao.findDepartmentByDeptCode(department.getDeptCode(), AppUtil.getCurrentUserOrgId());
        if(exitsInfo!=null && !exitsInfo.getId().equals(department.getId())){
            return  ResultVOUtils.error(ResultEnum.CODE_EXISTS.getStatus(),"该部门代码已经存在");
        }
        deptService.update(department);
        return ResultVOUtils.success();

    }
    @Log("新增部门")
    @RequestMapping(value = "/basicdata/department/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody Department department) {
        //判断该code是否已经存在
        Department exitsInfo = departmentDao.findDepartmentByDeptCode(department.getDeptCode(), AppUtil.getCurrentUserOrgId());
        if(exitsInfo!=null && !exitsInfo.getId().equals(department.getId())){
            return  ResultVOUtils.error(ResultEnum.CODE_EXISTS.getStatus(),"该部门代码们已经存在");
        }
        deptService.add(department);
        return ResultVOUtils.success();
    }

    @Log("查询部门（树结构）")
    @GetMapping("/basicdata/department/getProductTypesToTree/{id}")
    public ResultVO getProductTypesToTree(@PathVariable  Long id) {
        List<Object> objectList = deptService.getDepartmentsToTree(id);
        return ResultVOUtils.success(objectList);
    }

    @PostMapping("/basicdata/department/findDepartmentsByParentId")
    public ResultVO findDepartmentsByParentId(@RequestBody DeptVO deptVO) {
        List<Department> departmentList = deptService.findDepartmentByParentId(deptVO.getId(),deptVO.getOrgId());
        return ResultVOUtils.success(departmentList);
    }


    @GetMapping("/basicdata/department/findDepartmentById/{id}")
    public ResultVO findDepartmentById(@PathVariable Long id) {
        Department department = deptService.findDepartmentById(id);
        return ResultVOUtils.success(department);
    }
    @GetMapping("/basicdata/department/findDepartmentBylevel/{level}")
    public ResultVO findDepartmentBylevel(@PathVariable Integer level) {
        return ResultVOUtils.success(deptService.findDepartmentBylevel(level));
    }


    @PostMapping("/basicdata/department/updateProductTypeCodeLevelByDrag")
    public ResultVO updateDeptCodeLevelByDrag(@RequestBody ProductTypeHandleVO productTypeHandleVO) {
        Long id = deptService.updateDeptCodeLevelByDrag(productTypeHandleVO);
        return ResultVOUtils.success(id);
    }


}
