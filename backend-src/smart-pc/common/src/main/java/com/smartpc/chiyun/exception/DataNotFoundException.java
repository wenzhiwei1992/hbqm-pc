package com.smartpc.chiyun.exception;


import lombok.Data;

/**
 * @author zihao
 */
@Data
public class DataNotFoundException extends RuntimeException {
    private String code;

    private String msg;

    public DataNotFoundException(String msg){
        super(msg);
        this.msg = msg;
    }

}
