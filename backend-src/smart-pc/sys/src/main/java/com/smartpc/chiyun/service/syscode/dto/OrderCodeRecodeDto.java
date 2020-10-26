package com.smartpc.chiyun.service.syscode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 入库单，出库单 单号生成数据交互类
 *
 * @Author yue
 * @create 2020/7/16 5:58 下午
 */
@ApiModel("入库单，出库单,单号生成数据交互类")
@Data
public class OrderCodeRecodeDto {
    @ApiModelProperty("开始单号")
    private String codeNoStart;
    @ApiModelProperty("结束单号")
    private String codeNoEnd;
    @ApiModelProperty("单号长度")
    private Integer length;
    @ApiModelProperty("出入库类型")
    private Integer inOutType;

}
