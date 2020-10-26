package com.smartpc.chiyun.enums;

import lombok.Getter;

/**
 * @Author yue
 * @create 2020/3/30 5:13 下午
 * 用于质检打印
 */
@Getter
public enum CheckEnum {

    CPJ(1, "CPJ");

    private int code;
    private String value;

    CheckEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

}
