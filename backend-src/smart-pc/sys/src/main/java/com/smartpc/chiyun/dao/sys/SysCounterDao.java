package com.smartpc.chiyun.dao.sys;

import com.smartpc.chiyun.model.sys.SysCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysCounterDao extends JpaRepository<SysCounter, Long> {


    @Query("select e from SysCounter e where e.type = ?1 and e.orgId = ?2 ")
    SysCounter findByType(String type,Long orgId);
}
