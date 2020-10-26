
package com.smartpc.chiyun.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartpc.chiyun.model.CommonProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@ApiModel("用户实体")
@Table(name = "pc_user")
public class User extends CommonProperties implements UserDetails {
    public static final String SORT = "chiyun";

    public static final String DEFAULT_PASSWORD = "PassWord";

    public static final String ADMIN = "0";
    public static final String ORG = "1";
    public static final String GENERAL = "2";

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("真实用户名")
    private String realName;
    private String nickName;
    private String mobile;
    private String email;
    private String telephone;
    private String password;
    private String type;
    /**
     * 用户等级
     * 0.超级管理员
     * 1.组织管理员
     * 2.普通用户
     */
    @ApiModelProperty("用户等级")
    private String level;

    /**
     * 头像，企业微信同步
     */
    @ApiModelProperty("头像，企业微信同步")
    private String avatar;
    @ApiModelProperty("部门ID")
    private Long deptId;

    /**
     * 是否启用,security需要
     */
    @ApiModelProperty("是否启用,security需要")
    private Boolean enabled = true;


    /**
     * 是否启用浇筑确认
     */
    @JsonInclude()
    @Transient
    private Boolean jzqr = false;


    /**
     * 可查看权限的组织
     */
    @ApiModelProperty("可查看权限的组织")
    @Column(name = "org_ids")
    private String orgIds;

    @JsonInclude()
    @Transient
    private String tenantId;


    @JsonInclude()
    @Transient
    private String deptName;


    @JsonInclude()
    @Transient
    List<Group> groups;
    @JsonInclude()
    @Transient
    private List<Long> ids;
    @JsonInclude()
    @Transient
    private Long userGroupId;
    @JsonInclude()
    @Transient
    private Org org;
    @JsonInclude()
    @Transient
    private boolean orgIdNull;

    @JsonInclude
    @Transient
    private List<Long> orgIdsData;
    @JsonInclude
    @Transient
    private List<String> levelList;

    @JsonIgnore
    @Transient
    private Collection<GrantedAuthority> authorities;

    @JsonIgnore
    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    @Transient
    public boolean isEnabled() {
        return enabled;
    }

    @Transient
    public boolean isJZQR() {
        return jzqr;
    }

    /*判断用户app端 和 pc端进入 */
    @JsonIgnore
    @Transient
    private Boolean portFlag;


    /*JSessionId*/
    @JsonIgnore
    @Transient
    private String jSessionId;

    /*baseUrl 图片访问网络路径*/
    @JsonIgnore
    @Transient
    private String baseUrl;
    @JsonIgnore
    @Transient
    private String levelName;
    @Transient
    private String newPassword;
    @Transient
    private String confirmPassword;
    @Transient
    private String orgName;
}
