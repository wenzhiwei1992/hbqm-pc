package com.smartpc.chiyun.controller.sys;

import com.smartpc.chiyun.service.sys.service.WxService;
import com.smartpc.chiyun.utils.HttpUtil;
import com.smartpc.chiyun.utils.SignatureUtil;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.sys.WxInfo;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.Date;

@RestController
public class WxController {
    private static final Logger log = LoggerFactory.getLogger(WxController.class);
    @Autowired
    WxService wxService;

    @RequestMapping(value = "/jssdkInit", method = RequestMethod.GET)
    public SR<WxInfo> accessToken(String url,HttpSession session) throws Exception {
        String url2 =  URLDecoder.decode(url, "utf-8");
        SR<WxInfo> sr=new SR<>();
        url = url2;
        try{
            WxInfo wxInfo=(WxInfo)session.getAttribute("wxInfo");
            if(wxInfo!=null){
                Date lastTime = wxInfo.getLastTime();
                Date now=new Date();
                long diff=(now.getTime()-lastTime.getTime())/1000;
                if(diff<Long.parseLong(wxInfo.getJsApiTicketExpiresIn())){
                    sr.setEntity(wxInfo);
                    sr.setStatus(SR.SUCCESS);
                    return sr;
                }
            }
        }catch(Exception e){
            log.error(e.getMessage(),e);
            sr.setMsg("系统异常");
            return sr;
        }


        WxInfo info = wxService.initInfo();
        if(StringUtils.isEmpty(info.getCorpid())||StringUtils.isEmpty(info.getSecret())){
            sr.setMsg("企业微信配置异常");
            return sr;
        }
        //获取jsapi access token
        try{
            String addr=info.getApiUrl()+"/cgi-bin/gettoken"
                    +"?corpid="+info.getCorpid()
                    + "&corpsecret="+info.getSecret();
            String result = HttpUtil.get(addr);
            JSONObject json= new JSONObject(result);
            log.info("获取微信accesstoken---------------------"+json);
            if(json.isNull("errcode")){
                sr.setMsg("errcode not exist");
                return sr;
            }
            if(!json.getString("errcode").equals("0")){
                sr.setMsg(json.getString("errmsg"));
                return sr;
            }
            if(json.isNull("access_token")||
                    json.isNull("expires_in")){
                sr.setMsg("返回参数缺失");
                return sr;
            }

            info.setAccessToken(json.getString("access_token"));
            info.setExpiresIn(json.getString("expires_in"));
        }catch(Exception e){
            log.error(e.getMessage(),e);
            sr.setMsg("系统异常");
            return sr;
        }
        //获取jsapi ticket
        try{
            String addr=info.getApiUrl()+"/cgi-bin/get_jsapi_ticket"
                    +"?access_token="+info.getAccessToken();
            String result = HttpUtil.get(addr);
            JSONObject json= new JSONObject(result);
            log.info("获取微信jsapi ticket---------------------"+json);
            if(json.isNull("errcode")){
                sr.setMsg("errcode not exist");
                return sr;
            }
            if(!json.getString("errcode").equals("0")){
                sr.setMsg(json.getString("errmsg"));
                return sr;
            }
            if(json.isNull("ticket")||
                    json.isNull("expires_in")){
                sr.setMsg("返回参数缺失");
                return sr;
            }
            info.setJsApiTicket(json.getString("ticket"));
            info.setJsApiTicketExpiresIn(json.getString("expires_in"));

        }catch(Exception e){
            log.error(e.getMessage(),e);
            sr.setMsg("系统异常");
            return sr;
        }
        String timestamp = SignatureUtil.generateTimeStamp();
        info.setTimestamp(timestamp);
        String nonceStr = SignatureUtil.generateNonceStr();
        info.setNonceStr(nonceStr);
        String signature = SignatureUtil.generate(info.getJsApiTicket(), url, nonceStr, timestamp);
        info.setSignature(signature);
        info.setUrl(url);

        info.setLastTime(new Date());
        session.setAttribute("wxInfo",info);

        sr.setStatus(SR.SUCCESS);
        sr.setEntity(info);
        return sr;
    }

}
