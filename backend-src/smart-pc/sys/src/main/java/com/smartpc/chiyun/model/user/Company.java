package com.smartpc.chiyun.model.user;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公司信息表
 */
@Entity
@Getter
@Setter
@Table(name = "pc_company")
public class Company  extends CommonProperties {
    public static final String TABLE_NAME="pc_company";
    //公司名称
    @Column(name = "company_name")
    private String companyName;
    //工厂识别码
    @Column(name = "factory_id_code")
    private String factoryIdCode;
    @Column(name = "logo_url")
    private String logoUrl;

}
