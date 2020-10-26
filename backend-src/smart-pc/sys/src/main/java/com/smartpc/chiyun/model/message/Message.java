package com.smartpc.chiyun.model.message;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "message")
public class Message extends CommonProperties {

    @Column(name = "message_content")
    private String messageContent;
    @Column(name = "sender_Id")
    private String senderId;
    @Column(name = "receiver_Id")
    private String receiverId;
    @Column(name = "receiver_dd")
    private String receiverdd;

    @Column(name = "receiver_type")
    private Integer receiverType;

    @Column(name = "message_type")
    private Integer messageType;

    @Column(name = "send_type")
    private Integer sendType;
    @Column(name = "reamrk")
    private String reamrk;
    @Column(name = "status")
    private Integer status;


}
