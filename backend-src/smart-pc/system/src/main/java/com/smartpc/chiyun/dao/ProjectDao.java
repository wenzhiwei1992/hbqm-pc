
package com.smartpc.chiyun.dao;

import com.smartpc.chiyun.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDao extends JpaRepository<Project, Long> {

    /**
     * 通过id 查询项目
     *
     * @param id
     * @return
     */
    Project findProjectById(Long id);

    /**
     * 通过id批量删除项目数据
     *
     * @param ids
     */
    @Modifying
    @Query("delete from Project p where p.id in ?1")
    void deleteProjectsWithIds(List<Long> ids);

    Page<Project> findAll(Specification<Project> spc, Pageable pageable);


    @Query(value = "SELECT id, product_name,typwpe_id ,product_no  FROM pr_product where project_id = ?1", nativeQuery = true)
    List<Object[]> findProductIdByProjectId(Long projectId);


    Project findByProjectName(String name);

    @Query(value = "SELECT  a  FROM  Project a  where a.orgId in ?1  ")
    List<Project> findProjectsByOrgId(List<Long> orgIds);

    @Query("select e from  Project e where  e.orgId in ?1 ")
    List<Project> findAllByOrgId(List<Long> orgIds);


    @Query("select e from  Project e where e.projectNo = ?1 and  e.orgId = ?2 ")
    List<Project> findByProjectNo(String projectNo, Long orgId);


}
