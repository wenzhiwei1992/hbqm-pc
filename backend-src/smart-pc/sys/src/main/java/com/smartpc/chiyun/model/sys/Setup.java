package com.smartpc.chiyun.model.sys;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "pc_setup")
public class Setup extends CommonProperties {
    public static final String TABLE_NAME="pc_setup";

    @Column(name = "default_active")
    private String defaultActive;

    /**
     * 是否开启多组织
     *  true 为开启，
     *  false 为关闭
     */
    private Boolean enabledMultiOrg;

    /**
     * 添加企业微信信息
     */
    private String corpid;

    private String corpsecret;

    private Long deptId;

}
