package com.smartpc.chiyun.service.syscode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发货单单号生成数据交互类
 *
 * @Author yue
 * @create 2020/7/16 6:10 下午
 */
@ApiModel("发货单单号生成数据交互类")
@Data
public class OrderCodeInvoiceDto {
    @ApiModelProperty("开始单号")
    private String codeNoStart;
    @ApiModelProperty("结束单号")
    private String codeNoEnd;
    @ApiModelProperty("长度")
    private Integer length;
}
