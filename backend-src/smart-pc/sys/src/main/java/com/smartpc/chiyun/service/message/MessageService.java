package com.smartpc.chiyun.service.message;


import com.smartpc.chiyun.dao.message.MessageDao;
import com.smartpc.chiyun.dao.message.MessageTemplateDao;
import com.smartpc.chiyun.dao.user.UserGroupDao;
import com.smartpc.chiyun.dao.user.UserM2EpWxDao;
import com.smartpc.chiyun.model.message.Message;
import com.smartpc.chiyun.model.message.MessageTemplate;

import com.smartpc.chiyun.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class MessageService {
    @Autowired
    UserM2EpWxDao userM2EpWxDao;
    @Autowired
    UserGroupDao userGroupDao;
    //TODO 模块拆分注释，后续删除

    /*   @Autowired
    DingDingConfigService dingDingConfigService;*/
    @Autowired
    MessageDao messageDao;

    @Autowired
    MessageTemplateDao messageTemplateDao;

    public Message getSendMessageInfo(MessageTemplate template, int platform) {

        String[] idArr = template.getReceiverId().split(",");
        String thridPartyIds = "";
        if (platform == 1) {//1微信

        } else if (platform == 2) {//2钉钉
            List<String> userIds = null;
            if (template.getReceiverType() == 0) {//个人
                userIds = userM2EpWxDao.findByUserIdsAndType(idArr, platform);
            } else if (template.getReceiverType() == 1) {//角色
                userIds = userM2EpWxDao.findByTypeAndGroupIds(idArr, platform);
            }
            thridPartyIds = StringUtil.listToStr(userIds, ",");

        } else {

        }
        Message message = new Message();
        message.setMessageType(platform);
        message.setReceiverType(template.getReceiverType());
        message.setReceiverId(template.getReceiverId());
        message.setReceiverdd(thridPartyIds);
        message.setSendType(0);

        return message;
    }
    //TODO 模块拆分注释，后续删除
  /*  @Transactional
    public void sendMessage(Message message,Long orgId) {
        //钉钉消息最多2048个字节 并建议500字符以内 数据库是3000个字节
        //这里认为过长的消息没有意义 所以截断数据并补充后为500往左
        if(message.getMessageContent().length()>=682){
            message.setMessageContent(message.getMessageContent().substring(0,480)+"......");
            message.setMessageContent(message.getMessageContent()+"\n\n消息过长，请登录系统查看详细情况");
        }
        MessageData messageData = new MessageData();
      //  messageData.setAgent_id(533534758L);
        messageData.setUserid_list(message.getReceiverdd());

        MessageDD msg  = new MessageDD("text");
        msg.setText(new TextMessage(message.getMessageContent()));
        messageData.setMsg(msg);
        String taskId = dingDingConfigService.sendBaseMessage(messageData,orgId);
        message.setReamrk(taskId);
        message.setStatus(1);

        messageDao.save(message);
    }*/


}
