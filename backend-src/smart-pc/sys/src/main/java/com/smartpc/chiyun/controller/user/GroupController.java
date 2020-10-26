package com.smartpc.chiyun.controller.user;

import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.config.security.service.PermissionService;
import com.smartpc.chiyun.dao.user.GroupDao;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.service.user.GroupService;
import com.smartpc.chiyun.service.user.UserGroupService;
import com.smartpc.chiyun.model.BaseQuery;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.user.Group;
import com.smartpc.chiyun.model.user.Org;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
public class GroupController {
    @Autowired
    GroupDao d;
    @Autowired
    GroupService s;
    @Autowired
    UserGroupService userGroupService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    OrgDao orgDao;

    @Log("用户组删除")
    @RequestMapping(value = "/group/{id}", method = RequestMethod.DELETE)
    public SR delete(@PathVariable Long id) {
        SR sr = new SR();
        s.delete(id);
        //删除还要删除user_group 和 group_permission
        userGroupService.deleteByGroupId(id);
        permissionService.deleteByGroupId(id);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("用户组删除")
    @Transactional(rollbackForClassName = "Exception")
    @RequestMapping(value = "/group/list", method = RequestMethod.DELETE)
    public SR deletes(String ids) {
        String[] split = ids.split(";");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        SR sr = new SR();
        d.deletes(list);
        for (Long id : list) {
            //删除还要删除user_group 和 group_permission
            userGroupService.deleteByGroupId(id);
            permissionService.deleteByGroupId(id);
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @RequestMapping(value = "/group/{id}", method = RequestMethod.GET)
    public SR<Group> load(@PathVariable Long id) {
        Optional<Group> opt = d.findById(id);
        SR<Group> sr = new SR<>();
        sr.setStatus(SR.SUCCESS);
        opt.ifPresent(new Consumer<Group>() {
            @Override
            public void accept(Group entity) {
                entity.setUsers(userGroupService.groupUserList(id));
                sr.setEntity(entity);
            }
        });
        return sr;
    }

    @Log("查询用户组列表")
    @RequestMapping(value = "/group/list", method = RequestMethod.POST)
    public SR<Page<Group>> list(@RequestBody BaseQuery<Group> query) {
        SR<Page<Group>> sr = new SR<>();
        Page<Group> pageableList = s.list(query);
        List<Group> content = pageableList.getContent();
        for (Group g : content) {
            g.setUsers(userGroupService.findUserByGroupOrgId(g));
            Optional<Org> byId = orgDao.findById(g.getOrgId());
            byId.ifPresent(org -> g.setOrg(byId.get()));
        }
        sr.setEntity(pageableList);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("更新用户组")
    @RequestMapping(value = "/group/update", method = RequestMethod.POST)
    public SR<Group> update(@RequestBody Group entity) {
        SR<Group> sr = new SR<>();
        Group query = d.findById(entity.getId()).get();
        if (query == null) {
            sr.setMsg("记录不存在");
            return sr;
        }

        String groupName = entity.getGroupName();
        if (StringUtils.isEmpty(groupName)) {
            sr.setMsg("名称不能为空");
            return sr;
        }
        Group save = d.save(entity);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("新增用户组")
    @RequestMapping(value = "/group/add", method = RequestMethod.POST)
    public SR<Group> add(@RequestBody Group entity) {
        SR<Group> sr = new SR<>();
        String groupName = entity.getGroupName();
        if (StringUtils.isEmpty(groupName)) {
            sr.setMsg("名称不能为空");
            return sr;
        }
        Group save = d.save(entity);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }


}
