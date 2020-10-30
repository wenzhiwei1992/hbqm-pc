package com.smartpc.chiyun.model;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Getter
@Setter
@Table(name = "pc_approval")
public class Approval extends CommonProperties {

    @Column(name = "approval_level")
    private String approvalLevel;

    @Column(name = "approval_status")
    private String approvalStatus;


    @Column(name = "province_id")
    private Long provinceId;


    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "association_id")
    private Long associationId;

    @Column(name = "code_type")
    private String codeType;

    @Column(name = "reason")
    private String reason;

    @Column(name = "approvaler_id")
    private Long approvalerId; //审批人

    @Transient
    private String nowCodeName;
    @Column(name = "approval_dept_id")
    private Long approvalDeptId;


}
