package com.smartpc.chiyun.service.sys.dto;

import com.smartpc.chiyun.model.sys.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

/**
 * @Author yue
 * @create 2020/3/12 5:14 下午
 */
@ApiModel("菜单交互实体")
@Data
public class MenuDto implements Serializable {

    private Long id;

    private String icon;


    private String index;

    private String title;
    /**
     * 是否是按钮授权
     * authMark为true 只有在<b>按钮</b>才可以,同时写法参照以前的写法
     * index: "tableExample:edit"
     * title: "编辑"
     */
    private Boolean authMark;

    /**
     * 父级ID,ID为0 则视为根菜单
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;
    /*菜单标识 0为超级管理员访问菜单 1为单组织管理员和普通用户访问菜单*/
    private Integer appId;
    /**
     *
     */
    @ApiModelProperty("0目录；1菜单；2按钮")
    private String type;
    private String path;
    /*标识 0为PC端 1为手机端*/
    @ApiModelProperty("标识0为PC端；1为手机端")
    private Boolean flag;
    /*组件*/
    @ApiModelProperty("组件")
    private String component;
    /*是否为标签页*/
    @ApiModelProperty("是否为标签页")
    private Boolean isTab;
    @ApiModelProperty("路由区分0菜单按钮1页面跳转")

    private Boolean isRoute;

    private List<Menu> menuItems;

}
