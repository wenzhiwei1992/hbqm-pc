
package com.smartpc.chiyun.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 储备项目
 */
@Entity
@Getter
@Setter
@Table(name = "pr_reserve_project")
public class ReserveProject extends CommonProperties {

    @Column(name = "project_no")
    private String projectNo;

    @Column(name = "land_num")
    private Integer landNum;//地块数量
    /**
     * 结构形式
     */
    @Column(name = "structural_style")
    private String structuralStyle;
    /**
     * 配建面积
     */
    @Column(name = "construction_area",precision = 8,scale = 2)
    private BigDecimal constructionArea;
    /**
     * 当年拟落实面积
     */
    @Column(name = "implemented_area",precision = 8,scale = 2)
    private BigDecimal implementedArea;

    /**
     * 全/精装修面积
     */
    @Column(name = "decoration_area",precision = 8,scale = 2)
    private BigDecimal decorationArea;

    /**
     * EPC项目个数
     */
    @Column(name = "epc_num")
    private Integer epcNum;
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
