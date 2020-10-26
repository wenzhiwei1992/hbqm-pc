package com.smartpc.chiyun.utils;

import cn.hutool.core.util.ObjectUtil;
import com.smartpc.chiyun.exception.DataNotFoundException;

/**
 * @Author yue
 * @create 2020/3/12 2:17 下午
 */
public class ValidationUtil {
    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter, Object value) {
        if (ObjectUtil.isNull(obj) || "".equals(obj)) {
            String msg = entity + " 不存在: " + parameter + " is " + value;
            throw new DataNotFoundException(msg);
        }
    }
    /**
     * 验证是否为邮箱
     * @param string
     * @return
     */
    public static boolean isEmail(String string) {
        if (string == null){
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return string.matches(regEx1);
    }
}
