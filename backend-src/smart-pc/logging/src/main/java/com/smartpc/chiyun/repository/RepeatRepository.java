package com.smartpc.chiyun.repository;

import com.smartpc.chiyun.domain.Log;
import com.smartpc.chiyun.domain.RepeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatRepository extends JpaRepository<RepeatEntity,Long>, JpaSpecificationExecutor<Log> {


    @Query(value = "SELECT * from sys_repeat  where md_String =?1 ORDER BY id DESC  LIMIT 1", nativeQuery = true)
    RepeatEntity findInfoByMdString(String md_String);
}
