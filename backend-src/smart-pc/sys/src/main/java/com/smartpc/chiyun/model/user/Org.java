package com.smartpc.chiyun.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pc_org")
public class Org extends CommonProperties {

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "org_desc")
    private String orgDesc;

    @Column(name = "address")
    private String address;

    @Column(name = "production_type")
    private String productionType;

    @Column(name = "lineNum")
    private Integer lineNum;

    @Column(name = "design_productivity",precision = 8,scale = 2)
    private BigDecimal designProductivity;

    @Column(name = "real_productivity",precision = 8,scale = 2)
    private BigDecimal realProductivity;

    @Column(name = "state")
    private String state;


    @Column(name = "approval_status")
    private String approvalStatus;


    @Column(name = "province_id")
    private Long provinceId;


    @Column(name = "city_id")
    private Long cityId;


    /**
     * 经纬度
     */
    @Column(name = "longitude")
    private String longitude;

    @Column(name = "proposer")
    private String proposer;

    @Column(name = "proposer_phone")
    private String proposerPhone;



    @Transient
    private String provinceName;


    @Transient
    private String cityName;


    @JsonInclude()
    @Transient
    List<User> users;
    @JsonInclude()
    @Transient
    List<Long> ids;
    /**
     * 用于用户隔离，组织管理员和普通用户只能查看当前组织的信息
     */
    @JsonInclude()
    @Transient
    private List<Long> orgIds;
}
