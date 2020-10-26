package com.smartpc.chiyun.utils;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 判断字符串不为空
     */
    public static boolean isNotEmpty(Object str) {
        return (str != null && !str.equals(""));
    }

    /**
     * 判断字符串不为空
     */
    public static boolean isNotNullAndEmpty(String str) {
        return (str != null && !"".equals(str.trim()) && !"null".equals(str));
    }

    public static boolean isNullOrEmpty(String str) {
        return (str == null || "".equals(str.trim()) && !"null".equals(str));
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * 数组转字符串
     *
     * @param array
     * @param separator
     * @return
     */
    public static String join(Object[] array, String separator) {

        if (array == null) {
            return null;
        }
        int startIndex = 0;
        int endIndex = array.length;
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * 判断字符串是否是正整数  不含0
     *
     * @param num
     * @return
     * @author 李强
     * @date 2020年5月28日15:21:10
     */
    public static boolean checkNumInt(String num) {
        /*
         * Regexp匹配模式
         */
        String num1 = "^[1-9]$";

        Pattern pattern1 = Pattern.compile(num1);

        Matcher matcher1 = pattern1.matcher(num);

        return matcher1.matches();
    }

    /**
     * 判断字符串是否是数字
     *
     * @param num
     * @param size 小数点后XX位  最多支持多少位
     * @return
     * @author 李强
     * @date 2017年8月4日
     */
    public static boolean checkNum(String num, int size) {
        /*
         * Regexp匹配模式
         */
        String num1 = "^[0-9]+(.[0-9]{0," + size + "})?$";

        Pattern pattern1 = Pattern.compile(num1);

        Matcher matcher1 = pattern1.matcher(num);

        return matcher1.matches();
    }

    /**
     * 校验字符串是否是数值(包含小数与负数)
     * 示例：
     * false : . 1. 1sr -  12. -12.
     * true: -12 -12.0 -12.056 12 12.0 12.056
     *
     * @param str 需要校验的字符串
     * @return false :不是数值 true：是数值
     */
    public static boolean checkNumber(String str) {
        String regex = "-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?";
        if (str == null || !str.matches(regex)) {
            return false;
        }
        return true;
    }


    /**
     * list转字符串
     *
     * @param list
     * @param ContentPlaceHolder
     * @return
     */
    public static String listToStr(List list, String ContentPlaceHolder) {
        if (list.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Object data : list) {
            sb.append(data.toString()).append(ContentPlaceHolder);
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 替换字符串中占位符
     *
     * @param str
     * @param params
     * @return
     */
    public static String replaceParams(String str, Map<String, Object> params) {
        Matcher matcher = matcher(str);
        if (matcher.find()) {
            while ((matcher = matcher(str)).find()) {
                str = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
            }
        }
        return str;
    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(str);
    }


    /**
     * 将字符串按指定字符分隔成list
     *
     * @param str       源字符串
     * @param splitChar 　分隔字符
     * @return
     * @date 2016年11月23日
     */
    public static List<Long> splitToLong(String str, String splitChar) {
        List<Long> list = null;
        if (str != null && !str.isEmpty()) {
            list = new ArrayList<Long>();
            String[] strs = str.split(splitChar);
            for (String string : strs) {
                list.add(Long.parseLong(string));
            }
        }
        return list;
    }

    /**
     * obj 转 list
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

}
