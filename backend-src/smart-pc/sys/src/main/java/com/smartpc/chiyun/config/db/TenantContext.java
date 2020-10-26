package com.smartpc.chiyun.config.db;


import com.smartpc.chiyun.controller.user.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static ThreadLocal<String> currentTenant = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return System.getProperty("database");
        }
    };

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        String t = currentTenant.get();
        return t;
    }

    public static void clear() {
        currentTenant.remove();
    }
}
