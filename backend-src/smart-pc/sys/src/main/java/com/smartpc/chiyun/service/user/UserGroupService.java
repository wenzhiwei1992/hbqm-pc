package com.smartpc.chiyun.service.user;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.user.GroupDao;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.dao.user.UserGroupDao;
import com.smartpc.chiyun.model.user.Group;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.model.user.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGroupService {
    @Autowired
    UserGroupDao userGroupDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GroupDao groupDao;

    @Transactional
    public List<UserGroup> deleteByUserIdAndGroupId(Long userId, Long groupId) {
        return userGroupDao.deleteByUserIdAndGroupId(userId, groupId);
    }

    @Transactional
    public void deleteByGroupIdAndUserIdIn(String groupId, List<Long> userIds) {
        userGroupDao.deleteByGroupIdAndUserIdIn(groupId, userIds);
    }

    public List<User> groupUserList(Long groupId) {
        return findUserByGroupId(groupId);
    }

    public List<Group> userGroupList(Long userId) {
        UserGroup ug = new UserGroup();
        ug.setUserId(userId);
        List<UserGroup> ugs = userGroupDao.findAll(Example.of(ug));
        List<Long> groupIds = new ArrayList<Long>();
        for (UserGroup u : ugs) {
            groupIds.add(u.getGroupId());
        }
        List<Group> groups = groupDao.findByIdIn(groupIds);
        return groups;
    }

    @Transactional
    public void deleteByGroupId(Long id) {
        userGroupDao.deleteByGroupId(id);
    }

    @Transactional
    public void deleteByUserId(Long id) {
        userGroupDao.deleteByUserId(id);
    }

    /**
     * 超级管理员：查询除普通用户外的用户
     * 组织管理员：查询用户通过角色和当前角色所属的组织
     *
     * @param group
     * @return
     */
    public List<User> findUserByGroupOrgId(Group group) {
        List<User> users = findUserByGroupId(group.getId());
        User user = AppUtil.getCurrentUser();
        if (user != null) {
            if (User.ADMIN.equals(user.getLevel())) {
                users = users.stream().filter(user1 ->
                        !User.GENERAL.equals(user1.getLevel())
                ).collect(Collectors.toList());
            } else if (User.ORG.equals(user.getLevel()) || User.GENERAL.equals(user.getLevel())) {
                users = users.stream().filter(user1 ->
                        group.getOrgId().equals(user1.getOrgId())
                ).collect(Collectors.toList());
            }
        }
        return users;
    }

    private List<User> findUserByGroupId(Long groupId) {
        UserGroup ug = new UserGroup();
        ug.setGroupId(groupId);
        List<UserGroup> ugs = userGroupDao.findAll(Example.of(ug));
        List<Long> userIds = new ArrayList<Long>();
        for (UserGroup u : ugs) {
            userIds.add(u.getUserId());
        }
        List<User> users = userDao.findByIdIn(userIds);
        return users;
    }
}
