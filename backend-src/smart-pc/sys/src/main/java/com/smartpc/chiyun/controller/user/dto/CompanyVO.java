package com.smartpc.chiyun.controller.user.dto;

import com.smartpc.chiyun.config.PageInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyVO {
    private String companyName;
    //工厂识别码
    private String factoryIdCode;
    private PageInfo pageInfo;
}
