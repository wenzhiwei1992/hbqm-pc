package com.smartpc.chiyun.controller.message.dto;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class MessageTemplateDTO  {

    private Integer id;

    private String messageContent;

    private Integer receiverType;//0个人 1 部门 2 角色

    @Transient
    private String receiverTypeName;

    private String receiverId;

    @Transient
    private String receiverName;

    private Integer status;

    private String typeCode;

    @Transient
    private String messageContentTemplate;

    private String cron;


}
