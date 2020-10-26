package com.smartpc.chiyun.utils;

import cn.hutool.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 获取当前登录的用户
 * @date 2019-01-17
 */
public class SecurityUtils {

    public static Object getUserDetails() {
        Object obj;
        try {
            obj =org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getDetails();
        } catch (Exception e) {
            throw new RuntimeException(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        }
        return obj;
    }

    /**
     * 根据Principal()获取UserDetail
     * @return
     */
    public static UserDetails getUserDetailsByPrincipal() {
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new RuntimeException(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        }
        return userDetails;
    }

    /**
     * 获取系统用户名称
     * @return 系统用户名称
     */
    public static String getUsername(){
        Object obj = getUserDetails();
        return new JSONObject(obj).get("username", String.class);
    }

    /**
     * 系统获取用户等级
     * @return
     */
    public static String getLevel(){
        Object obj = getUserDetails();
        return new JSONObject(obj).get("level", String.class);
    }
}
