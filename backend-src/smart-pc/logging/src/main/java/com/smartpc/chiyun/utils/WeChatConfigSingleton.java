package com.smartpc.chiyun.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeChatConfigSingleton {

    public static final String SSO_URL ="https://open.weixin.qq.com/connect/oauth2/authorize";

    static class WeChatConfigSingletonHolder {

        static WeChatConfigSingleton instance = new WeChatConfigSingleton();

    }

    public static WeChatConfigSingleton getInstance() {

        return WeChatConfigSingletonHolder.instance;

    }

    public static String getAccessToken(String corpId,String secret) {

        //首先判断本地有无记录，记录是否过期 7200s
        boolean isExpired = WeChatSystemContext.getInstance().isExpired();

        if(isExpired) {

            //拼接url,APPID和APPSECRET从开发者中心获取
            String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+ corpId + "&corpsecret=" + secret;
            log.info("access_token url : ={}",url);

            //发起HTTPS的GET请求
            String jsonStr = HttpUtil.get(url);
            log.info("access_token json str : ={}",jsonStr);
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);

            if(jsonObject.containsKey("access_token")){
                String accessToken = jsonObject.get("access_token").toString();
                //记录到配置 access_token 当前时间
                WeChatSystemContext.getInstance().saveLocalAccessonToke(accessToken);
                return accessToken;
            }
            return null;
        } else {
            //从配置中直接获取access_token
            return WeChatSystemContext.getInstance().getAccessToken();

        }

    }
}
