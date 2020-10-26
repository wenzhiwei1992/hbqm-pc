package com.smartpc.chiyun.config.security.service;

import com.smartpc.chiyun.dao.user.GroupPermissionDao;
import com.smartpc.chiyun.dao.user.PermissionDao;
import com.smartpc.chiyun.model.user.Group;
import com.smartpc.chiyun.model.user.Permission;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.service.user.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "role")
public class PermissionService {

    @Autowired
    GroupPermissionDao groupPermissionDao;

    @Autowired
    PermissionDao permissionDao;

    private final GroupService groupService;

    public PermissionService(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
     * @param user 用户信息
     * @return Collection
     */
    @Cacheable(key = "'loadPermissionByUser:' + #p0.username")
    public Collection<GrantedAuthority> mapToGrantedAuthorities(User user) {

        System.out.println("--------------------loadPermissionByUser:" + user.getUsername() + "---------------------");

        Set<Group> groups = groupService.findAllByUserId(user.getId());
        Set<String> permissions= new HashSet<>();
        for (Group group : groups) {
            List<Permission> permission = group.getPermissions();
            List<String> permissionNames = permission.stream().map(Permission::getName).collect(Collectors.toList());
            permissions.addAll(permissionNames);

        }
        return permissions.stream().map(permission -> new SimpleGrantedAuthority(permission))
                .collect(Collectors.toList());
    }

    public List<Permission> loadTree() {
        List<Permission> trees = new LinkedList<>();
        List<Permission> permissionList = permissionDao.findAll();
        for (Permission permission : permissionList) {
            Long pid = permission.getPid();
            if (pid == 0){
                trees.add(permission);
            }
            for (Permission pm : permissionList) {
                if (permission.getId().equals(pm.getPid())){
                    if (permission.getChild() == null) {
                        permission.setChild(new ArrayList<Permission>());
                    }
                    permission.getChild().add(pm);
                }
            }
        }
        return trees;
    }

    @Transactional
    public void deleteByGroupId(Long id) {
        groupPermissionDao.deleteByGroupId(id);
    }

    public Permission loadById(Long id) {
        if (permissionDao.findById(id).isPresent()){
            return permissionDao.findById(id).get();
        }else return null;

    }

    /**
     * 加载所有pid为0 的节点
     * @return
     */
    public List<Permission> loadPids() {
        return  permissionDao.findAllByPid(0L);
    }

    public Permission add(Permission permission) {
        return permissionDao.save(permission);
    }

    public void deleteById(Long id) {
        permissionDao.deleteById(id);
    }
}
