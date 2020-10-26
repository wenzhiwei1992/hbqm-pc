package com.smartpc.chiyun.config.security;



import com.smartpc.chiyun.utils.EhcacheUtil;
import com.smartpc.chiyun.utils.RequestHolder;
import com.smartpc.chiyun.utils.StringUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

public class MyFilterSecurityInterceptor extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = RequestHolder.getHttpServletRequest();
            Enumeration<String> info =  request.getHeaderNames();
            String token = request.getHeader("jsessionid");
            if (StringUtil.isNotNullAndEmpty(token)) {
                EhcacheUtil ehcacheUtil = EhcacheUtil.getInstance();
                UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) ehcacheUtil.get("pdaUser", token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        // 继续调用 Filter 链
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
