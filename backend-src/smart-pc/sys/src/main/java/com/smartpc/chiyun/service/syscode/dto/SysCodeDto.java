package com.smartpc.chiyun.service.syscode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典类获取状态位数据交互对象
 *
 * @Author yue
 * @create 2020/6/23 1:36 下午
 */
@ApiModel("字典类获取状态位数据交互对象")
@Getter
@Setter
public class SysCodeDto {
    @ApiModelProperty(value = "字典编码",required = true)
    private String codeNo;
    @ApiModelProperty(value = "组织ID")
    private Long orgId;

}
