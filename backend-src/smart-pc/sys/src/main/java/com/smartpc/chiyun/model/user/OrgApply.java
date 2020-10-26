package com.smartpc.chiyun.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pc_org_apply_history")
public class OrgApply extends CommonProperties {


    @Column(name = "approval_status")
    private String approvalStatus;


    @Column(name = "province_id")
    private Long provinceId;


    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "approvaler_id")
    private String approvalerId;

    @Column(name = "reason")
    private String reason;

}
