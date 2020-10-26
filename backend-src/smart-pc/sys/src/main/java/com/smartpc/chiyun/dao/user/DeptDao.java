package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeptDao extends JpaRepository<Dept, Long> {

    @Modifying
    @Query("delete from Dept d where d.id in ?1")
    void deleteWithIds(List<Long> ids);
}
