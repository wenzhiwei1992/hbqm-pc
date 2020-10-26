package com.smartpc.chiyun.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 获取 HttpServletRequest
 *
 * @author zihao
 */
public class RequestHolder {

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }


    /**
     * 通过request获取ip
     *
     * @return
     */
    public static String getIp() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServerName();
    }


    /**
     * 通过request获取端口
     *
     * @return
     */
    public static String getLocalPort() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServerPort() + "";
    }

    /**
     * 通过request获取ip+端口
     *
     * @return
     */
    public static String getIpAndPort() {
        return "http://" + getIp() + ":" + getLocalPort();
    }
}
