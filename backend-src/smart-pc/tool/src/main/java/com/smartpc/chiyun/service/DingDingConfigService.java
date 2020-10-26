package com.smartpc.chiyun.service;

import com.smartpc.chiyun.config.dingding.CorpUserDetail;
import com.smartpc.chiyun.config.dingding.message.MessageData;
import com.smartpc.chiyun.model.message.Message;
import org.springframework.stereotype.Service;


@Service
public interface DingDingConfigService {

    /**
     * 获取当前登陆人的钉钉相关信息
     *
     * @return
     */
    public String getAccessToken();

    /**
     * 获取指定组织的钉钉相关信息
     *
     * @return
     */
    public String getAccessToken(Long orgId);


    public String getJsapiTicket();

    public String getJsapiTicket(Long orgId);

    /**
     * 根据免登授权码查询免登用户userId
     *
     * @param code
     * @return
     * @throws Exception
     */
    public String getUserInfo(String code, Long orgId);

    /**
     * 根据手机号获取企业下的员工id
     *
     * @param phone
     * @return
     */
    public String getUserInfoByPhone(String phone);


    /**
     * 创建钉钉用户
     *
     * @param userDetail
     * @return
     * @throws Exception
     */
    public String createUser(CorpUserDetail userDetail) throws Exception;


    /**
     * 更新钉钉用户信息
     *
     * @param userDetail
     * @return
     * @throws Exception
     */
    public String updateUser(CorpUserDetail userDetail) throws Exception;


    /**
     * 更新钉钉用户信息
     *
     * @return
     * @throws Exception
     */
    public String deleteUser(String userid) throws Exception;


    /**
     * 根据免登授权码查询免登用户userId
     *
     * @return
     * @throws Exception
     */
    public String getUnionIdByUserId(String userid);


    public String sendBaseMessage(MessageData messageData, Long orgId);


    public String findMessageStatus(MessageData messageData);

    void sendMessage(Message message, Long orgId);

//    public JSONObject sendDDMessage(Long agentId, String userId, Message message);


}
