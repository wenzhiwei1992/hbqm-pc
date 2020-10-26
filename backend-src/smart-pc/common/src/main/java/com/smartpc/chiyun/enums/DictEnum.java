package com.smartpc.chiyun.enums;

import lombok.Getter;

@Getter
public enum DictEnum {
    /**
     * 第一个状态值
     */
    FIRST,
    /**
     * 构件类型状态
     */
    PRODUCT_PROCESS_STATUS,
    /**
     * 已完成构件状态值
     */
    PRODUCT_STATUS_ORDER_OVER,

    /**
     * 未完成构件状态值
     */
    PRODUCT_STATUS_WWC,
    /**
     * 产出量
     */
    PRODUCT_STATUS_OUT_PUT,
    /**
     * 未生产构件状态值
     */
    PRODUCT_STATUS_WSC,
    /**
     * 生产中构件状态值
     */
    PRODUCT_STATUS_SCZ,

    /**
     * 统计在库存中的构件状态
     */
    PRODUCT_STATUS_IN_STOCK,
    /**
     * 构件检验状态
     */
    PRODUCT_CHECK_STATUS,
    /**
     * 成品检所有的状态位
     */
    CHECK_RESULT_CPJ_ALL,
    /**
     * 成品检返修状态位
     */
    CHECK_RESULT_CPJ_FX,
    /**
     * 成品检返修对应的构件状态
     */
    PRODUCT_STATUS_CPJ_FX,
    /**
     * 未发货
     */
    PRODUCT_STATUS_NOT_FH,
    /**
     * 已发货
     */
    PRODUCT_STATUS_YFH,
    /**
     * 半成品入库
     */
    SFG_WAREHOUSING_STATUS,
    /**
     * 半成品出库
     */
    SFG_PICKING_STATUS,
    /*生产方式*/
    PRODUCTION;

}
