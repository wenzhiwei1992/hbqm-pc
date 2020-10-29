package com.smartpc.chiyun.model.user.vo;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
public class ApprovalVO {

    private String dateCreated;

    private String approvalLevel;

    private String approvalStatus;

    private String approvalerName;

    private String deptName;

    private String reason;


}
