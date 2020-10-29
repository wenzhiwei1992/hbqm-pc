package com.smartpc.chiyun.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author yue
 * @create 2019/11/19 2:15 下午
 * 项目发货进度表
 */
@Getter
@Setter
public class ProjectProgressVO {
    /**
     * 项目下构件总数量
     */
    private Integer productTotalNumber;
    /**
     * 项目下在库数量
     */
    private Integer productWarehousingNumber;
    /**
     * 项目下已发货数量
     */
    private Integer productShipNumber;
    /**
     * 项目下待发货数量
     */
    private Integer productWaitShipNumber;
    /**
     * 项目下构件总方量
     */
    private Double productTotalVol;
    /**
     * 项目下构件在库方量
     */
    private Double productWarehousingVol;
    /**
     * 项目下已发货方量
     */
    private Double productShipVol;
    /**
     * 项目下待发货方量
     */
    private Double productWaitShipVol;
    private Long projectId;
    private String projectName;

}
