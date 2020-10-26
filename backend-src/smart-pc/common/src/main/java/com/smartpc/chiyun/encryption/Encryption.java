package com.smartpc.chiyun.encryption;


import org.apache.commons.codec.digest.DigestUtils;

public class Encryption {
    public static String getEncryptionPassword(String password) {
        String passwordMD5 = DigestUtils.md5Hex(password);
        //此处加密后加盐再进行加密
        return DigestUtils.md5Hex(passwordMD5 + "chiyun");
    }
}
