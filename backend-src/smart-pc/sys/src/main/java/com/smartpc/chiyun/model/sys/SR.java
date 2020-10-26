package com.smartpc.chiyun.model.sys;


public class SR<T> {
    public static final String SUCCESS="success";
    public static final String FAIL="fail";

    public static final String FAIL_TYPE_SYSTEM="system";
    public static final String FAIL_TYPE_BIZ="biz";
    public static final String FAIL_TYPE_BIZ_INPUT_ILLEGAL="biz_input_illegal";
    public static final String FAIL_TYPE_BIZ_AUTH_FAIL="biz_auth_fail";
    public static final String FAIL_TYPE_BIZ_RESULT_FAIL="biz_result_fail";
    public static final String FAIL_TYPE_REQUEST_PARAMETER_ERROR ="request_parameter_error";

    private String status;
    private String msg;
    private T entity;
    private String failType;
    private String failSubTye;


    public SR(){
        this.status=SR.FAIL;
        this.failType=this.FAIL_TYPE_BIZ;
    };
    public String getFailType() {
        return failType;
    }
    public void setFailType(String failType) {
        this.failType = failType;
    }
    public String getFailSubTye() {
        return failSubTye;
    }
    public void setFailSubTye(String failSubTye) {
        this.failSubTye = failSubTye;
    }
    public SR(String defaultStatus, String defaultMsg){
        this.status=defaultStatus;
        this.msg=defaultMsg;
    }
    public T getEntity() {
        return entity;
    }
    public void setEntity(T entity) {
        this.entity = entity;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setStatus(String status,String failType) {
        this.status = status;
        this.failType=failType;
    }
    public void setStatus(String status,String failType,String failSubType) {
        this.status = status;
        this.failType=failType;
        this.failSubTye=failSubType;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
