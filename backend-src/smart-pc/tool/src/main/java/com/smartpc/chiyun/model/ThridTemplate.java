package com.smartpc.chiyun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartpc.chiyun.config.PageInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sys_thrid_template")
@Data
public class ThridTemplate extends CommonProperties {

    /**
     * 系统代码
     */
    @Column(name = "template_no")
    private String  templateNo;

    /**
     * 外键ID
     */
    @Column(name = "template_type_id")
    private Long  templateTypeId;
    /**
     * 三方地址
     */
    @Column(name = "template_path")
    private String templatePath;


    @JsonInclude()
    @Transient
    private String typeName;

    @JsonInclude()
    @Transient
    private PageInfo pageInfo;

}
