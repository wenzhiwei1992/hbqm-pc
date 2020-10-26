package com.smartpc.chiyun.model.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartpc.chiyun.model.CommonProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@ApiModel("菜单实体")
@Entity
@Table(name = "sys_menu")
@Data
public class Menu extends CommonProperties {
    public final static String MENU_TYPE_ML = "0";
    public final static String MENU_TYPE_CD = "1";
    public final static String MENU_TYPE_AN = "2";
    @ApiModelProperty("图标")
    @Column(name = "icon")
    private String icon;
    @ApiModelProperty("索引")
    @Column(name = "index_name")
    private String index;
    @ApiModelProperty("标题")
    private String title;
    /**
     * 是否是按钮授权
     * authMark为true 只有在<b>按钮</b>才可以,同时写法参照以前的写法
     * index: "tableExample:edit"
     * title: "编辑"
     */
    @ApiModelProperty("是否是按钮授权")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean authMark;

    /**
     * 父级ID,ID为0 则视为根菜单
     */
    @ApiModelProperty("父级id")
    private Long parentId;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
    /*菜单标识 0为超级管理员访问菜单 1为单组织管理员和普通用户访问菜单*/
    @ApiModelProperty("菜单标识:0为超级管理员访问菜单;1为单组织管理员和普通用户访问菜单")
    @Column(name = "app_id")
    private Integer appId;
    /**
     * 0 目录  1 菜单 2 按钮
     */
    @ApiModelProperty("0目录；1菜单；2按钮")
    private String type;
    @ApiModelProperty("路径")
    private String path;
    /*标识 1为PC端 0为手机端*/
    @ApiModelProperty("标识1为PC端；0为手机端")
    private Boolean flag;
    /*组件*/
    @ApiModelProperty("组件")
    private String component;
    /*是否为标签页*/
    @ApiModelProperty("是否为标签页")
    @Column(name = "is_tab")
    private Boolean isTab;
    @ApiModelProperty("路由区分0菜单按钮1页面跳转,区分菜单加载和路由加载数据")
    @Column(name = "is_route")
    private Boolean isRoute;
    @ApiModelProperty(value = "外链菜单")
    @Column(name = "i_frame")
    private Boolean iFrame;
    @ApiModelProperty(value = "名称")
    @Column(name = "name")
    private String name;

    @Column(columnDefinition = "bit(1) default 0")
    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    @ApiModelProperty("子级菜单")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private List<Menu> menuItems;

    @ApiModelProperty("菜单显示noredirect")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String redirect;

}
