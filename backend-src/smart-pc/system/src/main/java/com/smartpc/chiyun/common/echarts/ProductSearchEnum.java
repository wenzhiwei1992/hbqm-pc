package com.smartpc.chiyun.common.echarts;

import lombok.Getter;

@Getter
public enum ProductSearchEnum {
    NOTCOMPLATED("未完成"),
    COMPLATED("已完成"),
    INSTOCK("已入库"),
    DELIVERED("已发货"),

    ;
    private String status;

    ProductSearchEnum(String status){
        this.status = status;
    }
}
