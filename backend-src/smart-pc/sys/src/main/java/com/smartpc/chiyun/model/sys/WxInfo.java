package com.smartpc.chiyun.model.sys;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WxInfo {
    private String apiUrl;
    private String corpid;
    private String secret;
    /*
     * token info
     */
    private String accessToken;
    private String expiresIn;
    /*
     * jsapi
     */
    private String jsApiTicket;
    private String jsApiTicketExpiresIn;
    private String timestamp;
    private String nonceStr;
    private String signature;
    private String url;
    private Date lastTime;

}
