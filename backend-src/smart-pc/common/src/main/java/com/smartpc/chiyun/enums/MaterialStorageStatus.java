package com.smartpc.chiyun.enums;

import lombok.Getter;

/**
 * 原料枚举
 */
@Getter
public enum MaterialStorageStatus {
    NOTCHECK(0,"待检验"),
    UPTOSTANDARD(10,"合格"),
    NOTUPTOSTANDARD(20,"退回"),
    CONCESSION(25,"让步接收"),
    CONCESSIONAPPROVAL(26,"待审批"),
    NOTWAREHOUSING(30,"待入库"),
    SOMEWAREHOUSING(35,"部分入库"),
    WAREHOUSING(40,"已入库"),
    NOTDELIVERGOODS(50,"待发料"),
    DELIVERGOODS(60,"已发料"),
    RETURN(70,"已退料"),
    TURNDOWN(80,"驳回"),
    ;
    private Integer code;
    private String status;
    MaterialStorageStatus(Integer code,String status){
        this.code = code;
        this.status = status;
    }

}
