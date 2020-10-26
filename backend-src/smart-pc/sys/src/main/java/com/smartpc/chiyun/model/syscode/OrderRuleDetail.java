package com.smartpc.chiyun.model.syscode;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zihao
 */
@Table(name = "ma_order_rule_d")
@Entity
@Data
public class OrderRuleDetail extends CommonProperties {


    /**
     * 系统代码,关联外键
     */
    @Column(name = "rule_id")
    private Long ruleId;


    /**
     * 系统代码,关联外键
     */
    @Column(name = "dict_code_id")
    private Long dictCodeId;

    @Column(name = "dict_code_name")
    private String dictCodeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;
}
