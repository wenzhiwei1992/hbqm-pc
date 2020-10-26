package com.smartpc.chiyun.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.config.dingding.CorpUserDetail;
import com.smartpc.chiyun.config.dingding.message.MessageDD;
import com.smartpc.chiyun.config.dingding.message.MessageData;
import com.smartpc.chiyun.config.dingding.message.TextMessage;
import com.smartpc.chiyun.dao.message.MessageDao;
import com.smartpc.chiyun.dao.syscode.CodeExplainContentDao;
import com.smartpc.chiyun.http.PostDataTools;
import com.smartpc.chiyun.model.message.Message;
import com.smartpc.chiyun.service.DingDingConfigService;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.utils.StringUtils;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.utils.EhcacheUtil;
import com.smartpc.chiyun.config.consts.CacheConsts;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.*;

@Log4j2
@Repository
public class DingDingConfigServiceImpl implements DingDingConfigService {

    @Autowired
    CodeExplainContentDao codeExplainContentDao;
    @Autowired
    MessageDao messageDao;

    /**
     * 调整到1小时50分钟
     */
    public static final long cacheTime = 1000 * 60 * 55 * 2;


    /**
     * 获取AccessToken和Jsapi_ticket
     *
     * @param codeNo
     * @param codeName
     * @return
     */
    public String getAccessTokenFormDataBase(String codeNo, String codeName) {
        String reamrk = "";
        List<CodeExplainContent> configs = codeExplainContentDao.findCodeExplainContentByCodeNoAndCodeName(codeNo, codeName);
        if (configs != null && configs.size() > 0) {
            //根据时间差判断是否应该过期
            Long time = new Date().getTime();
            Long diff = time - configs.get(0).getDateModified().getTime();
            if (diff < cacheTime) {
                reamrk = configs.get(0).getRemark();
            }

        }
        return reamrk;
    }


    public Map<String, Object> getDingDingConfigFormDataBase(String codeNo, Long orgId) {
        List<CodeExplainContent> configs = codeExplainContentDao.findCodeExplainContentByCodeNoAndOrgId(codeNo, orgId);
        Map<String, Object> configMap = new HashMap<>();
        if (!configs.isEmpty()) {
            String cacheName = CacheConsts.DDCONFIG;
            String cacheKey = CacheConsts.DDCONFIG;
            EhcacheUtil ehcacheUtil = EhcacheUtil.getInstance();
            for (CodeExplainContent config : configs) {
                configMap.put(config.getCodeName(), config.getRemark());
            }
            ehcacheUtil.put(cacheName, cacheKey, configMap);
        }
        return configMap;
    }

//    public String getDingDingConfigFormDataBase(String codeNo,String codeName) {
//        List<CodeExplainContent> configs = codeExplainContentDao.findCodeExplainContentByCodeNoAndCodeName(codeNo,codeName);
//        if(configs!=null && configs.size()>0){
//            return configs.get(0).getRemark();
//        }
//        return null;
//    }

    public String getAccessToken() {
        if (AppUtil.getCurrentUserOrgId() == null) {
            return null;
        }
        return getAccessToken(AppUtil.getCurrentUserOrgId());
    }


    public String getDDConfigByOrgIdAndKey(Long orgId, String key) {
        String cacheName = CacheConsts.DDCONFIG;
        String cacheKey = CacheConsts.getDDConfig(orgId);
        EhcacheUtil ehcacheUtil = EhcacheUtil.getInstance();

        Object obj = ehcacheUtil.get(cacheName, cacheKey);

        //字符串转map
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(obj));
        Map<String, Object> map = (Map<String, Object>) jsonObject;//json对象转Map
        if (obj != null) {
            if (StringUtil.isNotNullAndEmpty(map.get(key).toString())) {
                return map.get(key).toString();
            }
        }
        return "";
    }

    @Override
    public String getAccessToken(Long orgId) {
        // TODO 自动生成的方法存根
        String access_token = "";
        String cacheName = CacheConsts.DDCONFIG;
        String cacheKey = CacheConsts.getDDConfig(orgId);
        EhcacheUtil ehcacheUtil = EhcacheUtil.getInstance();

        Object obj = ehcacheUtil.get(cacheName, cacheKey);

        //字符串转map
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(obj));
        Map<String, Object> map = (Map<String, Object>) jsonObject;//json对象转Map
        if (obj != null) {
            if (StringUtil.isNotNullAndEmpty(map.get("AccessToken").toString())) {
                String sessionCode = map.get("AccessToken").toString();
                return sessionCode;
            }
        }

        try {
            //去数据库获取 ddAccessToken
            map = getDingDingConfigFormDataBase("DingDingConfig", orgId);
            String appKey = map.get("AppKey").toString();
            String appSecret = map.get("AppSecret").toString();

            PostDataTools post = PostDataTools.getIstance();
            String result = post
                    .sendMessageNullParam(
                            "https://oapi.dingtalk.com/gettoken?appkey=" + appKey +
                                    "&appsecret=" + appSecret,
                            RequestMethod.GET,
                            new String[]{"Content-Type:text/html;charset=utf-8"},
                            null);
            if (StringUtils.isNotBlank(result)) {
                JSONObject object = JSON.parseObject(result);
                if (StringUtil.isNotNullAndEmpty(object.get("errmsg") + "") && (object.get("errmsg") + "").equals("ok")) {//成功
                    access_token = object.get("access_token") + "";
                    map.put("AccessToken", access_token);
                    ehcacheUtil.put(cacheName, cacheKey, map);
                    //保存到数据库
                    codeExplainContentDao.updateCodeExplainContentRemark(access_token, "DingDingConfig", "AccessToken");
                }
            }

        } catch (Exception e) {
            log.error(e);
        }
        return access_token;
    }


    public String getJsapiTicket() {
        if (AppUtil.getCurrentUserOrgId() == null) {
            return null;
        }
        return getJsapiTicket(AppUtil.getCurrentUserOrgId());
    }

    @Override
    public String getJsapiTicket(Long orgId) {
        // TODO 自动生成的方法存根
        String ddjsapi_ticket = "";
        try {
            ddjsapi_ticket = getAccessTokenFormDataBase("DingDingConfig", "Jsapi_ticket");
            if (!StringUtil.isNotNullAndEmpty(ddjsapi_ticket)) {//获取凭证
                String ddAccessToken = getAccessToken(orgId);
                PostDataTools post = PostDataTools.getIstance();
                String result = post
                        .sendMessageNullParam(
                                "https://oapi.dingtalk.com/get_jsapi_ticket?access_token=" +
                                        ddAccessToken,
                                RequestMethod.GET,
                                new String[]{"Content-Type:text/html;charset=utf-8"},
                                null);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject object = JSON.parseObject(result);
                    if (StringUtil.isNotNullAndEmpty(object.get("errmsg") + "") && (object.get("errmsg") + "").equals("ok")) {//成功
                        ddjsapi_ticket = object.get("ticket") + "";
                        codeExplainContentDao.updateCodeExplainContentRemark(ddjsapi_ticket, "DingDingConfig", "Jsapi_ticket");
                    }
                }

            }
        } catch (Exception e) {
            log.error(e);
        }
        return ddjsapi_ticket;
    }

    /**
     * 计算当前请求的jsapi的签名数据<br/>
     * <p>
     * 如果签名数据是通过ajax异步请求的话，签名计算中的url必须是给用户展示页面的url
     *
     * @param request
     * @return
     */
    public String getConfig(HttpServletRequest request) {
        String urlString = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        String queryStringEncode = null;
        String url;
        if (queryString != null) {
            queryStringEncode = URLDecoder.decode(queryString);
            url = urlString + "?" + queryStringEncode;
        } else {
            url = urlString;
        }
        /**
         * 确认url与配置的应用首页地址一致
         */
        System.out.println(url);

        /**
         * 随机字符串
         */
        String nonceStr = "chiyun";
        long timeStamp = System.currentTimeMillis() / 1000;
        String signedUrl = url;
        String accessToken = null;
        String ticket = null;
        String signature = null;

        try {
            accessToken = getAccessToken();
            ticket = getJsapiTicket();
            signature = sign(ticket, nonceStr, timeStamp, signedUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> configValue = new HashMap<>();
        configValue.put("jsticket", ticket);
        configValue.put("signature", signature);
        configValue.put("nonceStr", nonceStr);
        configValue.put("timeStamp", timeStamp);
        configValue.put("corpId", "CORP_ID");//企业id
        configValue.put("agentId", "AGENT_ID");//AGENT_ID

        String config = JSON.toJSONString(configValue);
        return config;
    }


    public static String sign(String ticket, String nonceStr, long timeStamp, String url) throws Exception {
        try {
            return getJsApiSingnature(url, nonceStr, timeStamp, ticket);
        } catch (Exception e) {
            log.info("签名失败", e);
        }
        return null;
    }

    public static String getJsApiSingnature(String url, String nonce, Long timeStamp, String jsTicket) throws Exception {
        String plainTex = "jsapi_ticket=" + jsTicket + "&noncestr=" + nonce + "&timestamp=" + timeStamp + "&url=" + url;
        String signature = "";

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(plainTex.getBytes("UTF-8"));
        signature = byteToHex(crypt.digest());
        return signature;

    }

    private static String byteToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        byte[] var2 = hash;
        int var3 = hash.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            formatter.format("%02x", b);
        }

        String result = formatter.toString();
        formatter.close();
        return result;
    }


    /**
     * 根据免登授权码查询免登用户userId
     *
     * @param code
     * @return
     * @throws Exception
     */
    @Override
    public String getUserInfo(String code, Long orgId) {
        String accessToken = getAccessToken(orgId);
        if (!StringUtil.isNotNullAndEmpty(accessToken)) {
            return null;
        }
        String url = "https://oapi.dingtalk.com/user/getuserinfo?" +
                "access_token=" + accessToken + "&code=" + code;

        PostDataTools post = PostDataTools.getIstance();
        String result = post
                .sendMessageNullParam(
                        url,
                        RequestMethod.GET,
                        new String[]{"Content-Type:text/html;charset=utf-8"},
                        null);
        if (StringUtils.isNotBlank(result)) {
            JSONObject object = JSON.parseObject(result);
            if (StringUtil.isNotNullAndEmpty(object.get("errmsg") + "") && (object.get("errmsg") + "").equals("ok")) {//成功
                return object.get("userid") + "";
            }
        }
        return null;
    }


    /**
     * 创建钉钉用户
     *
     * @param userDetail
     * @return
     * @throws Exception
     */
    @Override
    public String createUser(CorpUserDetail userDetail) throws Exception {
        String accessToken = getAccessToken();
        String url = "https://oapi.dingtalk.com/user/create?access_token=" + accessToken;
        PostDataTools post = PostDataTools.getIstance();
        String result = post.sendMessageInfo(url, userDetail, new String[]{"Content-Type:application/json;charset=utf-8"}, true);

        if (StringUtils.isNotBlank(result)) {
            JSONObject object = JSON.parseObject(result);
            System.out.println("object = " + object);
            if (StringUtil.isNotNullAndEmpty(object.get("userid") + "")) {//成功
                // 员工唯一标识ID
                return object.get("userid") + "";
            } else {
                return "error:" + object.get("errmsg");
            }
        }

        return null;
    }


    /**
     * 更新钉钉用户信息
     *
     * @param userDetail
     * @return
     * @throws Exception
     */
    @Override
    public String updateUser(CorpUserDetail userDetail) throws Exception {
        String accessToken = getAccessToken();
        String url = "https://oapi.dingtalk.com/user/update?access_token=" + accessToken;
        PostDataTools post = PostDataTools.getIstance();
        String result = post.sendMessageInfo(url, userDetail, new String[]{"Content-Type:application/json;charset=utf-8"}, true);

        if (StringUtils.isNotBlank(result)) {
            JSONObject object = JSON.parseObject(result);
            if (StringUtil.isNotNullAndEmpty(object.get("errmsg") + "") && (object.get("errmsg") + "").equals("ok")) {//成功
                // 员工唯一标识ID
                return "success";
            }
        }

        return "error";
    }


    /**
     * 更新钉钉用户信息
     *
     * @return
     * @throws Exception
     */
    @Override
    public String deleteUser(String userid) throws Exception {
        String accessToken = getAccessToken();
        String url = "https://oapi.dingtalk.com/user/delete?access_token=" + accessToken;
        PostDataTools post = PostDataTools.getIstance();
        String result = post.sendMessageNullParam(
                url,
                RequestMethod.GET,
                new String[]{"Content-Type:text/html;charset=utf-8"},
                null);
        if (StringUtils.isNotBlank(result)) {
            JSONObject object = JSON.parseObject(result);
            if (StringUtil.isNotNullAndEmpty(object.get("errmsg") + "") && (object.get("errmsg") + "").equals("ok")) {//成功
                // 员工唯一标识ID
                return "success";
            }
        }
        return "error";
    }

    @Override
    public String getUserInfoByPhone(String phone) {
        String accessToken = getAccessToken();
        String url = "https://oapi.dingtalk.com/user/get_by_mobile?" +
                "access_token=" + accessToken + "&mobile=" + phone;

        PostDataTools post = PostDataTools.getIstance();
        String result = post
                .sendMessageNullParam(
                        url,
                        RequestMethod.GET,
                        new String[]{"Content-Type:text/html;charset=utf-8"},
                        null);
        if (StringUtils.isNotBlank(result)) {
            JSONObject object = JSON.parseObject(result);
            if (StringUtil.isNotNullAndEmpty(object.get("errmsg") + "") && (object.get("errmsg") + "").equals("ok")) {//成功
                return object.get("userid") + "";
            }
        }
        return null;
    }

    /**
     * 根据免登授权码查询免登用户userId
     *
     * @return
     * @throws Exception
     */
    @Override
    public String getUnionIdByUserId(String userid) {
        String accessToken = getAccessToken();
        String url = "https://oapi.dingtalk.com/user/get?" +
                "access_token=" + accessToken +
                "&userid=" + userid;

        PostDataTools post = PostDataTools.getIstance();
        String result = post
                .sendMessageNullParam(
                        url,
                        RequestMethod.GET,
                        new String[]{"Content-Type:text/html;charset=utf-8"},
                        null);
        if (StringUtils.isNotBlank(result)) {
            JSONObject object = JSON.parseObject(result);
            if (StringUtil.isNotNullAndEmpty(object.get("errmsg") + "") && (object.get("errmsg") + "").equals("ok")) {//成功
                return object.get("unionid") + "";
            }
        }
        return null;
    }


    @Override
    public String sendBaseMessage(MessageData messageData, Long orgId) {
        String accessToken = getAccessToken(orgId);
        String agentId = getDDConfigByOrgIdAndKey(orgId, "AgentId");
        messageData.setAgent_id(Long.parseLong(agentId));
        String url = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?" +
                "access_token=" + accessToken;
        //这里可能报错 因为不许传空
        PostDataTools post = PostDataTools.getIstance();
        String result = post.sendMessageInfo(url, messageData, new String[]{"Content-Type:application/json;charset=utf-8"}, true);
        log.info(result);
        if (StringUtils.isNotBlank(result)) {
            JSONObject object = JSON.parseObject(result);
            if (StringUtil.isNotNullAndEmpty(object.get("task_id") + "")) {//成功
                return object.get("task_id") + "";
            }
        }
        return null;
    }

    @Override
    public String findMessageStatus(MessageData messageData) {
        String accessToken = getAccessToken();
        String url = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendprogress?" +
                "access_token=" + accessToken;
        //这里可能报错 因为不许传空
        PostDataTools post = PostDataTools.getIstance();
        String result = post.sendMessageInfo(url, messageData, new String[]{"Content-Type:application/json;charset=utf-8"}, true);
        if (StringUtils.isNotBlank(result)) {
            JSONObject object = JSON.parseObject(result);
            log.info(object.toJSONString());
            if (StringUtil.isNotNullAndEmpty(object.get("errmsg") + "") && (object.get("errmsg") + "").equals("ok")) {//成功
                return object.get("progress") + "";
            }
        }

        return null;
    }

    @Transactional
    @Override
    public void sendMessage(Message message, Long orgId) {
        //钉钉消息最多2048个字节 并建议500字符以内 数据库是3000个字节
        //这里认为过长的消息没有意义 所以截断数据并补充后为500往左
        if (message.getMessageContent().length() >= 682) {
            message.setMessageContent(message.getMessageContent().substring(0, 480) + "......");
            message.setMessageContent(message.getMessageContent() + "\n\n消息过长，请登录系统查看详细情况");
        }
        MessageData messageData = new MessageData();
        //  messageData.setAgent_id(533534758L);
        messageData.setUserid_list(message.getReceiverdd());

        MessageDD msg = new MessageDD("text");
        msg.setText(new TextMessage(message.getMessageContent()));
        messageData.setMsg(msg);
        String taskId = this.sendBaseMessage(messageData, orgId);
        message.setReamrk(taskId);
        message.setStatus(1);

        messageDao.save(message);
    }


}
