package com.smartpc.chiyun.model.sys.vo;

import lombok.Data;

import java.util.List;

@Data
public class GroupPermissionVO {

    private Long groupId;

    private List<Long> permissionIds;
}
