package com.smartpc.chiyun.vo;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ResultSJ {


    private String code;

    private String err;

    private Object data;

    public ResultSJ(){}

    public ResultSJ(String err) {
        this.err =err;

    }

    public ResultSJ(String code, String err) {
        this.code=code;
        this.err =err;

    }

    public ResultSJ(String code, String err, Object data) {
        this.code=code;
        this.err =err;
        this.data= data;
    }

}
