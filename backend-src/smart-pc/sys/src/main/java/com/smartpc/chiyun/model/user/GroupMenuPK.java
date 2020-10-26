package com.smartpc.chiyun.model.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author yue
 * @create 2020/3/12 3:46 下午
 */
@Data
public class GroupMenuPK implements Serializable {

    private Long groupId;

    private Long menuId;
}
