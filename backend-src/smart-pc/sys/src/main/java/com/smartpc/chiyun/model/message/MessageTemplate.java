package com.smartpc.chiyun.model.message;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "message_template")
public class MessageTemplate {


    public static final String TYPE_YJ="CHECK_YJ";//隐检
    public static final String TYPE_CPJ="CHECK_CPJ";//成品检
    public static final String TYPE_DAY_CHECK="PRODUCT_CHECK_CPJ_YJ";//构件隐检/成品检每日质检信息
    public static final String TYPE_DAY_PLAN_PRODUCTVOL_PROJECT="PRODUCT_PLAN_VOL_PROJECT"; //每日项目产量日报
    public static final String TYPE_DAY_RECORD_PRODUCTVOL="PRODUCT_RECORD_VOL"; //每日发货量
    public static final String TYPE_DAY_PLAN_PRODUCTVOL="PRODUCT_PLAN_VOL"; //每日计划量
    public static final String TYPE_DAY_INVENTORY="MATERIAL_INVENTORY"; //每日原料库存预警

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "org_id")
    private Long orgId;
    @Column(name = "message_content")
    private String messageContent;
    @Column(name = "message_content_template")
    private String messageContentTemplate;
    @Column(name = "receiver_type")
    private Integer receiverType;
    @Column(name = "receiver_id")
    private String receiverId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "type_code")
    private String typeCode;


}
