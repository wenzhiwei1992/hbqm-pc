package com.smartpc.chiyun.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 权限是树结构
 * id   alias   create_time         name           pid
 * 2	用户管理	2018-12-03 12:28:19	USER_ALL	   0
 * 3	用户查询	2018-12-03 12:31:35	USER_SELECT	   2
 * 4	用户创建	2018-12-03 12:31:35	USER_CREATE	   2
 * 5	用户编辑	2018-12-03 12:31:35	USER_EDIT	   2
 * 6	用户删除	2018-12-03 12:31:35	USER_DELETE	   2
 *
 * 7	角色管理	2018-12-03 12:28:19	ROLES_ALL	    0
 * 8	角色查询	2018-12-03 12:31:35	ROLES_SELECT	7
 * 10	角色创建	2018-12-09 20:10:16	ROLES_CREATE	7
 * 11	角色编辑	2018-12-09 20:10:42	ROLES_EDIT	    7
 * 12	角色删除	2018-12-09 20:11:07	ROLES_DELETE	7
 */
@Entity
@Data
@Table(name = "pc_permission")
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    private String alias;

    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @NotNull
    private String  name;

    @NotNull
    private Long pid=0L;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Transient
    private List<Permission> child;
}
