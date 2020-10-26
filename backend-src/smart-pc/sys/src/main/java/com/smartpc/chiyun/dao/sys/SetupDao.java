package com.smartpc.chiyun.dao.sys;

import com.smartpc.chiyun.model.sys.Setup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SetupDao extends JpaRepository<Setup, Long>,JpaSpecificationExecutor<Setup> {
    @Query("select e from Setup e where e.orgId = ?1 ")
    List<Setup> findByOrgId(Long currentUserOrgId);

}
