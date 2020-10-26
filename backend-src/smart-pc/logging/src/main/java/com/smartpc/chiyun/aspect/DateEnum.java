package com.smartpc.chiyun.aspect;

public enum DateEnum {
    //这边主要是做分钟级别的控制以及秒级别的控制
    // checkHours为10小时 因为发现总有特殊情况（一般为3-5分钟）这里选择第二次的时候客户掉线
    seconds("1000"), minutes("60000"), hours("3600000"), checkTenHours("36000000","checkInfo");

    private static  final String basePath = "noRepeat";

    private String value;

    private String path;

    DateEnum(String value,String path) {
        this.value = value;
        this.path = path;
    }

    DateEnum(String value) {
        this.value = value;
        this.path = basePath;
    }

    public String getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }
}
