
package com.smartpc.chiyun.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;

/**
 * 宝德西韦德  license 和过期时间验证解密
 */
public class LicenseDecrypt {
    public LicenseDecrypt() {
    }

    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];

        for(int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }

        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }

    public static String generateCode(String md5){
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];

        for(int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }

        String salt = new String(cs2);
        return new String(cs1);

    }

    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String((new Hex()).encode(bs));
        } catch (Exception var3) {
            return null;
        }
    }

    public static String md5HexReturn(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String((new Hex()).encode(bs));
        } catch (Exception var3) {
            return null;
        }
    }

    public static String decodeString(String str) {
        if (str == null) {
            return "转换失败";
        } else {
            byte[] s = pack(str);

            String gbk;
            try {
                gbk = new String(s, "gbk");
            } catch (UnsupportedEncodingException var4) {
                gbk = "转换失败";
            }

            return gbk;
        }
    }

    public static byte[] pack(String str) {
        int nibbleshift = 4;
        int position = 0;
        int len = str.length() / 2 + str.length() % 2;
        byte[] output = new byte[len];
        char[] var5 = str.toCharArray();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            char v = var5[var7];
            byte n = (byte)v;
            if (n >= 48 && n <= 57) {
                n = (byte)(n - 48);
            } else if (n >= 65 && n <= 70) {
                n = (byte)(n - 55);
            } else {
                if (n < 97 || n > 102) {
                    continue;
                }

                n = (byte)(n - 87);
            }

            output[position] = (byte)(output[position] | n << nibbleshift);
            if (nibbleshift == 0) {
                ++position;
            }

            nibbleshift = nibbleshift + 4 & 7;
        }

        return output;
    }

    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 86400000L;
        long nh = 3600000L;
        long diff = endDate.getTime() - nowDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        return day + "天" + hour + "小时";
    }

    public static void main(String[] args) {

        String expireDateCode ="323030392d30392d32332032323a30303a3030";
        String expireDateValue = LicenseDecrypt.decodeString(expireDateCode);
        Date now = new Date();

        Date expireDate = null;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            expireDate = format.parse(expireDateValue);

            System.out.println("expireDate: "+expireDate.toString());
        } catch (Exception var13) {
            System.out.println("证书时间解析失败");
        }

        if (null != expireDate && now.after(expireDate)) {
            System.out.println("证书已过期（过期时间：" + expireDateValue + "）");
        }

        String code = LicenseDecrypt.generateCode("161f5ef3bf3740503764d074b5fd0422217854325c70b69a");

        System.out.println("code : "+code);

    }
}
