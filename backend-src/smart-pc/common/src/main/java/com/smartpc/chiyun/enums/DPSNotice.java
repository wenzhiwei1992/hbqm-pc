package com.smartpc.chiyun.enums;

import lombok.Getter;

@Getter
public enum DPSNotice {

    DPS_NOTICE_GQ("是"),
    DPS_NOTICE_NOT("否"),
    ;

    private String msg;

    DPSNotice(String msg){
        this.msg = msg;
    }
}
