
package com.smartpc.chiyun.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import sun.font.Decoration;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Getter
@Setter
@Table(name = "pr_project")
public class Project extends CommonProperties {
    @Column(name = "project_name")
    private String projectName;//项目名称
    @Column(name = "work_place")
    private String workPlace;//项目地址
    @Column(name = "customer")
    private String customer;//建设单位
    @Column(name = "project_no")
    private String projectNo;

    /**
     * 经纬度
     */
    @Column(name = "longitude")
    private String longitude;
    /**
     * 构件生产单位
     */
    @Column(name = "production_unit")
    private String productionUnit;
    /**
     * 装配面积
     */
    @Column(name = "assembly_rate",precision = 8,scale = 2)
    private BigDecimal assemblyArea;
    /**
     * 装配率
     */
    @Column(name = "assembly_area",precision = 8,scale = 2)
    private BigDecimal assemblyRate;

    /**
   * 工程规模 */
      @Column(name = "project_scale",precision = 8,scale = 2)
      private BigDecimal projectScale;

    /**
     * 施工单位
     */
    @Column(name = "undertaking")
    private String undertaking;

    /**
     * 工程规模
     */
    @Column(name = "project_scale")
    private String projectScale;


    /**
     * 工程类型
     */
    @Column(name = "project_type")
    private String projectType;

    /**
     * 结构形式
     */
    @Column(name = "structural_style")
    private String structuralStyle;


    /**
     * 开始时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;
    /**
     * 结束时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;


    /**
     * 工程进度
     */
    @Column(name = "project_progress")
    private String projectProgress;


    /**
     * 是否为全装修或装配化装修
     */
    @Column(name = "isFullDecoration")
    private Boolean isFullDecoration;


    /**
     * 是否为政府投资工程
     */
    @Column(name = "isInvested")
    private Boolean isInvested;

    /**
     * 是否采用工程总承包(EPC)
     */
    @Column(name = "isEPC")
    private Boolean isEPC;

    /**
     * 省
     */
    @Column(name = "province_id")
    private Long provinceId;


    /**
     * 市
     */
    @Column(name = "city_id")
    private Long cityId;

    /**
     * 审批状态
     */
    @Column(name = "approval_status")
    private String approvalStatus;

    /**
     * 审批部门
     */
    @Column(name = "approval_dept_id")
    private Long approvalDeptId;


    @Transient
    private String provinceName;
    @Transient
    private String cityName;
    @Transient
    private Boolean isApproval;
}
