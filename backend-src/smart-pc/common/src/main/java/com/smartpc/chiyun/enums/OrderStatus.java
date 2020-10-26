package com.smartpc.chiyun.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    ORDER_NEW(0,"新建"),
    ORDER_CONFIRM(10,"提交"),
    ORDER_SC(20,"收池"),
    ORDER_SCZ(21,"生产中"),
    ORDER_COMPLETED(30,"完成"),


    ORDER_D_ZXZ(22,"执行中"),
    ORDER_D_YWC(32,"已完成");


    public static String[] PC_ORDER_STATUS =  {ORDER_CONFIRM.getValue(),ORDER_SC.getValue(),ORDER_SCZ.getValue(),ORDER_D_YWC.getValue()};

    public static String[] ONE_KEY_ORDER_STATUS =  {ORDER_COMPLETED.getValue(),ORDER_SCZ.getValue()};
    private Integer code;

    private String value;

    OrderStatus(Integer code,String value){
        this.code = code;
        this.value=value;
    }
}
