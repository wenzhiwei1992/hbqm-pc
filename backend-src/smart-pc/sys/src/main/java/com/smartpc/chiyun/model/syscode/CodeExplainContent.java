package com.smartpc.chiyun.model.syscode;

import com.smartpc.chiyun.model.CommonProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zihao
 */
@ApiModel("字典类详情实体")
@Table(name = "sys_dict_d")
@Entity
@Data
public class CodeExplainContent extends CommonProperties {


    /**
     * 系统代码,关联外键
     */
    @ApiModelProperty("系统代码,关联外键")
    @Column(name = "dict_code_no")
    private String codeNo;

    /**
     * 代码名称
     */
    @ApiModelProperty("代码名称")
    @Column(name = "code_name")
    private String codeName;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 是否启用.不启用则获取下个 sort 的状态值
     */
    @ApiModelProperty("是否启用，不启用则获取下个sort的状态值")
    @Column(name = "is_enabled")
    private Boolean isEnabled;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
    /**
     * 图标
     */
    @Column(name = "home_tab_icon")
    private String homeTabIcon;
    /**
     * 组件名
     */
    @Column(name = "home_tab_template_name")
    private String homeTabTemplateName;
    /**
     * 模板
     */
    @Column(name = "home_tab_html")
    private String homeTabHtml;
    /**
     * ref
     */
    @Column(name = "home_tab_ref")
    private String homeTabRef;
}
