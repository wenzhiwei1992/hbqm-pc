package com.smartpc.chiyun.controller.user;

import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.dao.user.UserGroupDao;
import com.smartpc.chiyun.service.user.UserGroupService;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.user.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserGroupController {
    @Autowired
    UserGroupDao d;
    @Autowired
    UserGroupService s;

    @Log("用户组新增用户（单个）")
    @RequestMapping(value = "/userGroup/add", method = RequestMethod.POST)
    public SR<UserGroup> add(@RequestBody UserGroup entity) {
        SR<UserGroup> sr=new SR<>();
        UserGroup save = d.save(entity);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("用户组新增用户（多个）")
    @RequestMapping(value = "/userGroup/adds", method = RequestMethod.POST)
    public SR<List<UserGroup>> adds(@RequestBody List<UserGroup> list) {
        SR<List<UserGroup>> sr=new SR<>();
        List<UserGroup> userGroups=new ArrayList<>();
        for(UserGroup entity:list){
            UserGroup save = d.save(entity);
            userGroups.add(save);
        }
        sr.setEntity(userGroups);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("用户组移除指定用户（单个用户）")
    @RequestMapping(value = "/userGroup/deleteByUserIdAndGroupId", method = RequestMethod.DELETE)
    public SR<UserGroup> deleteByUserIdAndGroupId(Long userId,Long groupId) {
        SR<UserGroup> sr=new SR<>();
        s.deleteByUserIdAndGroupId(userId, groupId);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }
    @Log("用户组移除指定用户（多个用户）")
    @RequestMapping(value = "/userGroup/deleteByGroupIdAndUserIdIn", method = RequestMethod.DELETE)
    public SR<UserGroup> deleteByGroupIdAndUserIdIn(String groupId,String userIds) {
        SR<UserGroup> sr=new SR<>();
        String[] split = userIds.split(";");
        List<Long> ids=new ArrayList<>();
        for(String s:split){
            ids.add(Long.parseLong(s));
        }
        s.deleteByGroupIdAndUserIdIn(groupId,ids);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }




}
