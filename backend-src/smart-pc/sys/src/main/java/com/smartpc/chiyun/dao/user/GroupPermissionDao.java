package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.GroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupPermissionDao extends JpaRepository<GroupPermission, Long>, JpaSpecificationExecutor<GroupPermission> {

    @Query("select e from GroupPermission e where e.groupId = ?1 ")
    List<GroupPermission> findAllByGroupId(Long groupId);

    @Modifying
    @Query("delete from GroupPermission e where e.groupId = ?1 ")
    void deleteByGroupId(Long groupId);
}
