package com.smartpc.chiyun.model.sys.vo;


import lombok.Data;

/**
 * 企业微信数据对象
 */
@Data
public class QywxVO {

    /**
     * 企业微信 corpid
     */
    private String corpid;

    /**
     * 通讯录秘钥
     */
    private String  corpsecret;

    /**
     * 同步的部门ID，根部门为1
     */
    private Long deptId;

}
