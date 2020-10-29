
package com.smartpc.chiyun.dao;

import com.smartpc.chiyun.model.Project;
import com.smartpc.chiyun.model.ReserveProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveProjectDao extends JpaRepository<ReserveProject, Long> {

    /**
     * 通过id 查询儲備项目
     *
     * @param id
     * @return
     */
    ReserveProject findReserveProjectById(Long id);

    Page<ReserveProject> findAll(Specification<ReserveProject> spc, Pageable pageable);

    @Query("select e from  ReserveProject e where  e.orgId in ?1 ")
    List<ReserveProject> findAllByOrgIds(List<Long> orgIds);



    @Query("select e from  ReserveProject e where e.projectNo = ?1 and  e.orgId = ?2 ")
    List<ReserveProject> findByProjectNo(String projectNo, Long orgId);


}
