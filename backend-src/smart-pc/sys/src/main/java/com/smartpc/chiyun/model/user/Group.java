package com.smartpc.chiyun.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pc_group")
public class Group extends CommonProperties {
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "group_desc")
    private String groupDesc;
    @Column(name = "active")
    private String active;
    /**
     * 这个在数据表存的是菜单
     */
    @Column(name = "authorities", length = 16777216)
    private String authorities;

    @JsonInclude()
    @Transient
    List<User> users;
    @JsonInclude()
    @Transient
    List<Long> ids;

    @JsonInclude()
    @Transient
    Org org;

    @JsonInclude
    @Transient
    private List<Permission> permissions;
    @JsonInclude
    @Transient
    private List<GroupMenu> groupMenus;
    @JsonInclude
    @Transient
    private List<Long> orgIds;

}
