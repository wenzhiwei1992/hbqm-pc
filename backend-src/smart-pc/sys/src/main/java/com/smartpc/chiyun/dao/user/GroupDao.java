
package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupDao extends JpaRepository<Group, Long>,JpaSpecificationExecutor<Group> {
    @Modifying
    @Query("delete from Group g where g.id in ?1")
    void deletes(List<Long> ids);

    List<Group> findByIdIn(List<Long> ids);
}
