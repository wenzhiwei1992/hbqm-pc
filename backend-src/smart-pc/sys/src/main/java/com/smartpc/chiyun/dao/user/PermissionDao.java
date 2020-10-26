package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionDao extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    @Query("select e from  Permission  e where e.pid = ?1 ")
    List<Permission> findAllByPid(Long pid);
}
