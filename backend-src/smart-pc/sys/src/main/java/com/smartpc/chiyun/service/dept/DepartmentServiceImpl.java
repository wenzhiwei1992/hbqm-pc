package com.smartpc.chiyun.service.dept;

import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.dao.dept.DepartmentDao;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.dept.Department;
import com.smartpc.chiyun.model.user.vo.ProductTypeHandleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentDao departmentDao;

    @Transactional
    @Override
    public void deleteDeptById(Long id) throws DataNotFoundException {
        Long orgId = AppUtil.getCurrentUserOrgId();
        List<Department> departmentList = departmentDao.findDepartmentByParentId(id);
        if (departmentList.size() > 0) {
            throw new DataNotFoundException("该层级下存在数据");
        }
        departmentDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteDeptsWithIds(List<Long> ids) {
        departmentDao.deleteDepartmentsWithIds(ids);
    }

    @Override
    public Page<Department> pageableList(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getCurrentPage() - 1, pageInfo.getPageSize(), new Sort(Sort.Direction.DESC, "dateModified"));
        List<Long> orgIds = AppUtil.getCurrentUserOrgIds();
        return departmentDao.findAll(orgIds, pageable);
    }

    @Override
    public void add(Department department) {
        updateCodeLevelByParentId(department);
        departmentDao.save(department);
    }

    @Override
    public void update(Department department) {
        updateCodeLevelByParentId(department);
        departmentDao.save(department);
    }

    @Override
    public Optional<Department> getOne(Long id) {
        return departmentDao.findById(id);
    }

    @Override
    public List<Department> findAll() {

        return departmentDao.findAll();
    }

    @Override
    public List<Department> findByOrgId() {
        Long orgId = AppUtil.getCurrentUserOrgId();
        return departmentDao.findByOrgId(orgId);
    }

    @Override
    public List<Object> getDepartmentsToTree(Long id) {
        //默认查询出所有的一级类别
        List<Department> departmentList = departmentDao.findDepartmentsByParentIdIsNull(id);
        List<Object> list = new ArrayList<>();
        //指定节点选择框是否禁用为节点对象的某个属性值 boolean, function(data, node),用于前端树行crud按钮的控制
        for (Department department : departmentList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", department.getId());
            jsonObject.put("label", department.getDeptName());
            jsonObject.put("children", getChildren(department.getId(), id));
            list.add(jsonObject);
        }
        return list;


    }

    @Override
    public List<Department> findDepartmentByParentId(Long parentId, Long orgId) {
        List<Department> list = new ArrayList<>();
        if (!StringUtils.isEmpty(parentId)) {
            //前端id为0,为第一级'构件类型'
            if (parentId == 0) {
                list = departmentDao.findDepartmentsByParentIdIsNull(orgId);
            } else {
                list = departmentDao.findDepartmentByParentId(parentId);
            }
        }
        return list;
    }

    @Override
    public Department findDepartmentById(Long id) {
        return departmentDao.findDepartmentById(id);
    }

    @Override
    public List<Department> findDepartmentBylevel(Integer level) {
        return departmentDao.findDepartmentBylevel(level);
    }

    @Override
    public Long updateDeptCodeLevelByDrag(ProductTypeHandleVO productTypeHandleVO) {
        //TODO 待测试
        Long orgId = AppUtil.getCurrentUserOrgId();
        Department department = departmentDao.findDepartmentById(productTypeHandleVO.getDraggingNodeId());
        Department department1 = departmentDao.findDepartmentById(productTypeHandleVO.getDropNodeId());
        if (department != null && department1 != null) {
            if (Department.INNER.equals(productTypeHandleVO.getDropType())) {
                department.setParentId(department1.getId());
                //department1.getCodeLevel()==null为异常数据，此处只是作规避处理，inner是做引入该节点内操作，+1
                department.setCodeLevel(department1.getCodeLevel() == null ? 1 : department1.getCodeLevel() + 1);
            } else if (Department.AFTER.equals(productTypeHandleVO.getDropType()) || Department.BEFORE.equals(productTypeHandleVO.getDropType())) {
                //after和before为结束拖拽时最后进入的节点的上方或者下方，所以和结束拖拽时最后进入的节点同等级，要先找他们的父级
                department.setParentId(department1.getParentId());
                department.setCodeLevel(department1.getCodeLevel() == null ? 0 : department1.getCodeLevel());
            }
            departmentDao.save(department);
            //查询该等级下是否存在子级
            List<Department> departmentList = departmentDao.findDepartmentByParentId(department.getId());
            if (departmentList != null) {
                loadProductType(department.getCodeLevel(), departmentList, orgId);
            }
            return department.getId();
        }
        return Long.parseLong("0");
    }

    @Override
    public Department findDepartmentByDeptName(String deptName) {
        Long orgId = AppUtil.getCurrentUserOrgId();
        Department department = departmentDao.findDepartmentByDeptNameAndOrgId(deptName, orgId);
        return department;
    }

    /**
     * 拖拽递归更新等级
     *
     * @param codeLevel
     * @param
     */
    public void loadProductType(Integer codeLevel, List<Department> departmentList, Long orgId) {
        if (departmentList != null) {
            codeLevel++;
            for (Department department : departmentList) {
                department.setCodeLevel(codeLevel);
                List<Department> departmentList1 = departmentDao.findDepartmentByParentId(department.getId());
                loadProductType(codeLevel, departmentList1, orgId);
            }
            departmentDao.saveAll(departmentList);
        }
    }


    /**
     * 递归查询构件类型
     *
     * @param parentId
     * @return
     */
    public List<Object> getChildren(Long parentId, Long orgId) {
        //TODO 待测试
        List<Object> list = new ArrayList<>();
        List<Department> children = departmentDao.findDepartmentByParentId(parentId);
        for (Department typeInfo : children) {
            JSONObject obj = new JSONObject(true);
            obj.put("id", typeInfo.getId());
            obj.put("label", typeInfo.getDeptName());
            obj.put("children", getChildren(typeInfo.getId(), orgId));
            list.add(obj);
        }
        return list;
    }

    /**
     * 树信息赋值
     *
     * @param department
     */
    public void updateCodeLevelByParentId(Department department) {
        department.setState(1);
        department.setIfSync(0);
        //第一级为0
        if (StringUtils.isEmpty(department.getParentId())) {
            department.setCodeLevel(0);
            department.setParentId(null);
        } else {
            Integer codeLevel = 0;
            updateCodeLevelByParentId(department.getParentId(), codeLevel, department);
        }
    }

    /**
     * 递归更新等级
     *
     * @param
     * @param codeLevel
     */
    private void updateCodeLevelByParentId(Long parentIdId, Integer codeLevel, Department department) {
        Department d = departmentDao.findDepartmentById(parentIdId);
        codeLevel++;
        if (d.getParentId() == null) {
            department.setCodeLevel(codeLevel);
        } else {
            updateCodeLevelByParentId(d.getParentId(), codeLevel, department);
        }
    }

}
