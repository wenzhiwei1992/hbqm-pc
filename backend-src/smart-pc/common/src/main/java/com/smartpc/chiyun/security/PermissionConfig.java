package com.smartpc.chiyun.security;


import com.smartpc.chiyun.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "pc")
public class PermissionConfig {

    public Boolean check(String... permissions) {
        // 如果是匿名访问的，就放行
        String anonymous = "anonymous";
        if (Arrays.asList(permissions).contains(anonymous)) {
            return true;
        }
        String level = SecurityUtils.getLevel();
        //超级管理员,放行
        if (!StringUtils.isEmpty(level)) {
            if ("0".equals(level)) {
                return true;
            }
        }
        // 获取当前用户的所有权限
        List<String> pcPermissions = SecurityUtils.getUserDetailsByPrincipal().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return pcPermissions.contains("admin") || Arrays.stream(permissions).anyMatch(pcPermissions::contains);
    }
}
