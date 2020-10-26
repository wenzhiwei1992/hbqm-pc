package com.smartpc.chiyun.model.syscode;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author liqiang
 */
@Entity
@Table(name = "ma_order_rule")
@Data
public class OrderRule extends CommonProperties {

    /**
     * 关联编号id
     */
    @Column(name = "code_id")
    private String  codeId;
    /**
     * 关联编号名称
     */
    @Column(name = "code_name")
    private String codeName;

    /**
     * 关联编号code
     */
    @Column(name = "code_no")
    private String codeNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 编号示例
     */
    @Column(name = "order_no_code")
    private String orderNoCode;

    /**
     * 结束时间
     */
    @Column(name = "endTime")
    private Date endTime;


    @Transient
    private List<OrderRuleDetail> codeContents;

}
