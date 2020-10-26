package com.smartpc.chiyun.controller.user;

import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.dept.DepartmentDao;
import com.smartpc.chiyun.dao.syscode.CodeExplainContentDao;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.model.dept.Department;
import com.smartpc.chiyun.service.sys.service.FileService;
import com.smartpc.chiyun.service.user.OrgService;

import com.smartpc.chiyun.model.BaseQuery;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.user.Org;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
public class OrgController {
    @Autowired
    OrgDao d;
    @Autowired
    OrgService orgService;

    @Autowired
    FileService fileService;

    @Autowired
    DepartmentDao departmentDao;

    @Log("删除组织")
    @RequestMapping(value = "/org/{id}", method = RequestMethod.DELETE)
    public SR delete(@PathVariable Long id) {
        SR sr = new SR();
        orgService.delete(id);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("删除组织")
    @RequestMapping(value = "/org/list", method = RequestMethod.DELETE)
    public SR deletes(String ids) {
        String[] split = ids.split(";");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        SR sr = new SR();
        d.deletes(list);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public SR<Org> load(@PathVariable Long id) {
        Optional<Org> opt = d.findById(id);
        SR<Org> sr = new SR<>();
        sr.setStatus(SR.SUCCESS);
        opt.ifPresent(new Consumer<Org>() {
            @Override
            public void accept(Org entity) {
                entity.setUsers(null);
                sr.setEntity(entity);
            }
        });
        return sr;
    }

    @Log("查询组织列表")
    @RequestMapping(value = "/org/list", method = RequestMethod.POST)
    public SR<Page<Org>> list(@RequestBody BaseQuery<Org> query) {
        SR<Page<Org>> sr = new SR<>();

        Page<Org> pageableList = orgService.list(query);
        List<Org> content = pageableList.getContent();
        for (Org g : content) {
            g.setUsers(orgService.orgUserList(g.getId()));
            if(g.getProvinceId()!=null){
                Department d =  departmentDao.findDepartmentById(g.getProvinceId());
                if(d!=null){
                    g.setProvinceName(d.getDeptName());
                }
            }
            if(g.getCityId()!=null){
                Department d =  departmentDao.findDepartmentById(g.getCityId());
                if(d!=null){
                    g.setCityName(d.getDeptName());
                }
            }
        }
        sr.setEntity(pageableList);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("更新组织")
    @RequestMapping(value = "/org/update", method = RequestMethod.POST)
    public SR<Org> update(@RequestBody Org entity) {
        SR<Org> sr = new SR<>();
        Org query = d.findById(entity.getId()).get();
        if (query == null) {
            sr.setMsg("记录不存在");
            return sr;
        }
        String OrgName = entity.getOrgName();
        if (StringUtils.isEmpty(OrgName)) {
            sr.setMsg("名称不能为空");
            return sr;
        }

        Org save = d.save(entity);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }


    @Log("新增组织")
    @RequestMapping(value = "/org/add", method = RequestMethod.POST)
    public SR<Org> add(@RequestBody Org entity) {
        SR<Org> sr = new SR<>();
        String OrgName = entity.getOrgName();
        if (StringUtils.isEmpty(OrgName)) {
            sr.setMsg("名称不能为空");
            return sr;
        }
        Org save = d.save(entity);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @RequestMapping(value = "/org/all", method = RequestMethod.GET)
    public SR<List<Org>> all() {
        SR<List<Org>> sr = new SR<>();
        List<Org> list = d.findAll(new Sort(Sort.Direction.ASC, "orgName"));
        sr.setEntity(list);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @RequestMapping(value = "/org/allByOrgIds", method = RequestMethod.GET)
    public SR<List<Org>> allByOrgIds() {
        SR<List<Org>> sr = new SR<>();
        List<Long> orgIds = AppUtil.getCurrentUserOrgIds();
        List<Org> list = d.findAllById(orgIds);
        sr.setEntity(list);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("移除组织指定用户")
    @RequestMapping(value = "/org/removeUser", method = RequestMethod.DELETE)
    public SR deleteByUserIdAndGroupId(Long userId, Long orgId) {
        SR sr = new SR<>();
        orgService.removeUser(userId, orgId);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("新增组织指定用户")
    @RequestMapping(value = "/org/addUsers", method = RequestMethod.POST)
    public SR adds(@RequestBody List<User> list) {
        SR sr = new SR<>();
        orgService.addUsers(list);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @RequestMapping(value = "/org/listNotTrue", method = RequestMethod.GET)
    public SR<List<Org>> getOrgNotTrue() {
        SR<List<Org>> sr = new SR();
        List<Org> orgList = orgService.getOrgNotTrue();

        sr.setEntity(orgList);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @GetMapping("/org/findOrgByUser")
    public ResultVO findOrgByUser() {
        List<Org> orgList = orgService.findOrgByUser();
        return ResultVOUtils.success(orgList);
    }

}
