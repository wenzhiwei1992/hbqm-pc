package com.smartpc.chiyun.model.sys;

public class SRUtils<T> {

    public SR<T> success(){
        SR<T> sr = new SR<>();
        sr.setStatus("0");
        sr.setMsg(SR.SUCCESS);
        return sr;
    }


    public SR<T> success(T entity){
        SR<T> sr = new SR<>();
        sr.setStatus("0");
        sr.setMsg(SR.SUCCESS);
        sr.setEntity(entity);
        return sr;
    }
}
