package com.smartpc.chiyun.vo;

import lombok.Data;

/**
 * @Author yue
 * @create 2020/6/11 12:24 上午
 */
@Data
public class ResultVO {
    private String status;

    private String msg;

    private Object entity;

    public ResultVO() {
    }

    public ResultVO(String status, String msg) {
        this.status = status;
        this.msg = msg;

    }

    public ResultVO(String status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.entity = data;
    }
}
