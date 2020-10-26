package com.smartpc.chiyun.model.user;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(GroupPermissionPK.class)
@Data
@Table(name = "pc_group_permission")
public class GroupPermission implements Serializable {

    @Id
    @Column(name = "group_id")
    private Long groupId;

    @Id
    @Column(name = "permission_id")
    private Long permissionId;

}
