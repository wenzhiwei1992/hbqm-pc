package com.smartpc.chiyun.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供加，减，乘，除运算
 */
public class ArithUtil {

    /**
     *  * 提供精确加法计算的add方法
     *  * @param value1 被加数
     *  * @param value2 加数
     *  * @return 两个参数的和
     *  
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1+""));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2+""));
        return b1.add(b2).doubleValue();
    }

    /**
     *  * 提供精确减法运算的sub方法
     *  * @param value1 被减数
     *  * @param value2 减数
     *  * @return 两个参数的差
     *  
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1+""));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2+""));
        return b1.subtract(b2).doubleValue();
    }

    /**
     *  * 提供精确乘法运算的mul方法
     *  * @param value1 被乘数
     *  * @param value2 乘数
     *  * @return 两个参数的积
     *  
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     *  * 提供精确的除法运算方法div
     *  * @param value1 被除数
     *  * @param value2 除数
     *  * @param scale 精确范围
     *  * @return 两个参数的商
     *  * @throws IllegalAccessException
     *  
     */
    public static double div(double value1, double value2, int scale) throws IllegalArgumentException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param value 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String roundString(String value, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("小数位数必须是正整数");
        }
        BigDecimal b = new BigDecimal(value);
        return subZeroAndDot(b.setScale(scale, BigDecimal.ROUND_HALF_UP).toString());
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param value 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double roundDouble(double value, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("小数位数必须是正整数");
        }
        BigDecimal b = new BigDecimal(Double.toString(value));
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * java统计List集合中每个元素出现的次数
     *
     * @param items
     * @return
     */
    public static Map<String, Integer> frequencyOfListElements(List<String> items) {
        if (items == null || items.size() == 0) return null;
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String temp : items) {
            Integer count = map.get(temp);
            map.put(temp, (count == null) ? 1 : count + 1);
        }
        return map;
    }



    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     * @param d
     * @return
     */
    public static Double subZeroAndDot(Double d){
        String s = d.toString();
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return Double.valueOf(s);
    }
}
