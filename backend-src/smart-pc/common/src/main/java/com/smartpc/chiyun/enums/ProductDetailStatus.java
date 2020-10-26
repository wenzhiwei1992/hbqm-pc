package com.smartpc.chiyun.enums;

import lombok.Getter;

/**
 * 构件明细状态枚举值
 */
@Getter
public enum ProductDetailStatus {
    NOTSTART(0,"未生产"),
    WAITPROD(10,"待生产"),
    WAITHIDECHECK(20,"待隐检"),
    CHECKANDREWORK(30,"隐检返修"),
    WAITOVERCHECK(40,"待成品检"),
    OVERREWORK(50,"成品返修"),
    SCRAP(60,"报废"),
    WAITENTRY(70,"待入库"),
    COMPLATED(75,"已完成"),
    OVERPRODUCTENTRY(80,"成品入库"),
    PLANDELIVERY(85,"计划发货"),
    DELIVERD(90,"已发货"),
    ;

    private Integer code;

    private String value;

    ProductDetailStatus(Integer code,String value){
        this.code =code;
        this.value = value;
    }
}
