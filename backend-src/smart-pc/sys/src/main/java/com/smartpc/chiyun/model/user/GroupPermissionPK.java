package com.smartpc.chiyun.model.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupPermissionPK implements Serializable {

    private Long groupId;

    private Long permissionId;

}
