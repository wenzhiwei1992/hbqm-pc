
package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.GroupMenu;
import com.smartpc.chiyun.model.user.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserGroupDao extends JpaRepository<UserGroup, Long> {
    List<UserGroup> deleteByUserIdAndGroupId(Long userId, Long groupId);

    @Modifying
    @Query("delete from UserGroup u where u.groupId=?1 and u.userId in ?2")
    void deleteByGroupIdAndUserIdIn(String groupId, List<Long> userIds);

    @Query("select u from UserGroup u where  u.userId in ?1")
    List<UserGroup> findAllByUserId(Long id);

    @Modifying
    @Query("delete from UserGroup u where u.groupId =?1 ")
    void deleteByGroupId(Long id);

    @Modifying
    @Query("delete from UserGroup u where u.userId =?1 ")
    void deleteByUserId(Long id);

    @Query("select  g from  UserGroup g where g.userId = ?1")
    List<UserGroup> findGroupByUserId(Long userId);
}
