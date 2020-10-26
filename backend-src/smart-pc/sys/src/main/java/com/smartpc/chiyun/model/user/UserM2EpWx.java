package com.smartpc.chiyun.model.user;

import com.smartpc.chiyun.config.PageInfo;
import lombok.Data;

import javax.persistence.*;

/**
 * 企业微信用户 和 本地用户 映射表
 */
@Data
@Entity
@Table(name = "pc_user_m2_epwx",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "type"}),
        indexes = {@Index(name = "UniqueIndex_wx_user_id_",columnList = "wx_user_id", unique = true)}
)
public class UserM2EpWx {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "user_id",length = 100)
    private String userId;

    /**
     * 企业微信用户ID
     * 登录过程： 从微信获取到 企业微信的用户id ，根据该id 检索这张表，查到本地用户，进行登录
     */
    @Column(name = "wx_user_id")
    private String epWxUserId;


    /**
     * 三方平台类型
     * 1.企业微信 2钉钉  默认为微信
     */
    @Column(name = "type")
    private Integer type=1;

    @Column(name = "phone")
    private String phone;

    @Transient
    private PageInfo pageInfo;

    @Transient
    private String userName;
}
