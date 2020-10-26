package com.smartpc.chiyun.model.user;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "pc_user_group")
public class UserGroup extends CommonProperties {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "group_id")
    private Long groupId;
}
