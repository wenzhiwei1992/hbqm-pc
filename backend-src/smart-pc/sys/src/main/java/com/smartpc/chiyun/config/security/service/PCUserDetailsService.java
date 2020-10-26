package com.smartpc.chiyun.config.security.service;

import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.service.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zihao
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PCUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private final PermissionService permissionService;

    public PCUserDetailsService(UserService userService, PermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){

        List<User> byUsername = userService.findByUsername(username);
        if (byUsername.size() == 0) {
            throw new RuntimeException("账号不存在");
        } else {
            return createPCUser(byUsername.get(0));
        }
    }

    public UserDetails createPCUser(User user) {
        user.setAuthorities(permissionService.mapToGrantedAuthorities(user));
        return user;
    }
}
