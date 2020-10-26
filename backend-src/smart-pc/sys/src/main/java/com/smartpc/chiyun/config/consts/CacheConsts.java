package com.smartpc.chiyun.config.consts;

public class CacheConsts {

    public static final String DDCONFIG = "dingdingConfig";

    public static final String WXCONFIG = "weixinConfig";


    public static String getDDConfig(Long orgId){
        return DDCONFIG+"_"+orgId;
    }

    public static String getWXConfig(Long orgId){
        return WXCONFIG+"_"+orgId;
    }

}
