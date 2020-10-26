package com.smartpc.chiyun.model.sys;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "sys_counter",indexes = {
        @Index(name = "sys_counter_ref_index", columnList = "type", unique = false)
})
public class SysCounter extends CommonProperties {

    public static final String PRODUCT_DETAIL_NO_TYPE = "PDN";

    /**
     * 当前值
     */

    private Long currentValue = 1L;

    /**
     * 区分是用作什么的序号值
     */
    @Column(name = "type",length = 100)
    private String type;


}
