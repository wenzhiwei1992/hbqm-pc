package com.smartpc.chiyun.controller.user.dto;

import lombok.Data;

/**
 * 用于构建单点登录的DTO
 */
@Data
public class SsoDTO {

    public static  final  String SSO_GET_USER_BY_CODE = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";


    /**
     * 数据库指向
     */
    private String tenantId;


    /**
     * 企业ID
     */
    private String corpId;

    /**
     * 应用秘钥，用于获取 token
     */
    private String secret;

    /**
     * 用户的 code ，根据 code 获取user id
     *  https://work.weixin.qq.com/api/doc#90000/90135/91023
     */
    private String code;

}
