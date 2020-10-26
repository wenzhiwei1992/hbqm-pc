package com.smartpc.chiyun.service.user;

import com.smartpc.chiyun.model.user.Group;
import com.smartpc.chiyun.model.user.GroupMenu;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author yue
 * @create 2020/3/17 11:07 下午
 */
public interface GroupMenuService {

    void updateGroupPermission(Group resources);

    List<GroupMenu> findGroupMenusByGroupId(Long groupId);

    List<String> findIndexByGroupId(Long groupId);

    @Transactional
    void deleteGroupMenuByGroupId(Long groupId);
}
