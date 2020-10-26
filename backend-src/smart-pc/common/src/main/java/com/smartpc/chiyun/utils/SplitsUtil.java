package com.smartpc.chiyun.utils;



public class SplitsUtil {
    private String s;

    public SplitsUtil(String s){
        this.s = s;
    }

    public SplitsUtil() {
        this("unknow");
    }

    public String[] getStr(){
        //  \d 为正则表达式表示[0-9]数字
        String[] str_string = s.split("\\d");
        return str_string;
    }

    public int[] getNum(){
        // \D 为正则表达式表示非数字
        String[] num_string = s.split("\\D");
        String a = "";

        for(String m : num_string){
            a += m;
        }
        //将分离出的重新保存在新数组num中不要直接用num_string，  因为在正则表达式对字符串进行选择时若前面的几个字符不符合要求但num_string数组中仍会存有其位置 是空格
        String[] num = a.split("");
        int[] inte = new int[num.length];


        for(int i =0; i < num.length; i++){
            //将该数组中的数字存入int数组
            inte[i] = Integer.parseInt(StringUtil.isNotNullAndEmpty(num[i])?num[i]:"0");
        }

        return inte;
    }
    //输出字符串中的字母
    public String printStr(){
        String a = "";
        for(String n : getStr()){
            a += n;
        }
        return a;
    }
    //输出字符串中的数字
    public String printNum(){
        String a = "";
        for(int n : getNum()){
            a += n;
        }
       return a;
    }

}
