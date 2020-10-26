package com.smartpc.chiyun.config.web;

import com.smartpc.chiyun.config.db.TenantContext;
import com.smartpc.chiyun.model.sys.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;



/**
 * 把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
 */
@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    ServerConfig serverConfig;

    private static final String TENANT_HEADER = "X-TenantID";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {
        Enumeration<String> headerNames = req.getHeaderNames();

        String tenant = req.getHeader(TENANT_HEADER);
        if (!StringUtils.isEmpty(tenant) && !tenant.startsWith("[")) {
            TenantContext.setCurrentTenant(tenant);
        } else {
            TenantContext.setCurrentTenant(System.getProperty("database"));
        }

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        TenantContext.clear();
    }





}
