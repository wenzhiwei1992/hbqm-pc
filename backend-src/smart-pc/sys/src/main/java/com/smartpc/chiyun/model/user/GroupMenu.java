package com.smartpc.chiyun.model.user;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author yue
 * @create 2020/3/12 3:06 下午
 */

@Data
@Entity
@IdClass(GroupMenuPK.class)
@Table(name = "pc_group_menu")
public class GroupMenu implements Serializable {
    @Id
    @Column(name = "group_id")
    private Long groupId;
    @Id
    @Column(name = "menu_id")
    private Long menuId;
}
