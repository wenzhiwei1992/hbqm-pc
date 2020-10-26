package com.smartpc.chiyun.model.syscode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.model.CommonProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @Author yue
 * @create 2020/2/19 1:13 下午
 */

@Table(name = "ma_templet")
@Data
@Entity
public class Templet extends CommonProperties {
    @Column(name = "templet_no")
    private String templetNo;
    @Column(name = "templet_name")
    private String templetName;
    @Column(name = "templet_json", length = 4000)
    private String templetJson;

    private String remark;
    @JsonInclude()
    @Transient
    private PageInfo pageInfo;
}
