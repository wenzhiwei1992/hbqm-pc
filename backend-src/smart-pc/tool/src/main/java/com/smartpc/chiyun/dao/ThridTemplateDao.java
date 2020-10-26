package com.smartpc.chiyun.dao;

import com.smartpc.chiyun.model.ThridTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zihao
 */
@Repository
public interface ThridTemplateDao extends JpaRepository<ThridTemplate,Long>,JpaSpecificationExecutor<ThridTemplate> {

    @Modifying
    @Query("delete from ThridTemplate u where u.id in ?1")
    void deleteThridTemplatesWithIds(List<Long> list);

    @Query("select u from ThridTemplate u where u.templateNo = ?1 and u.templateTypeId=?2")
    ThridTemplate findThridTemplateByParam(String templateNo,Long templateTypeId);


    @Query("select u from ThridTemplate u where u.templateNo = ?1 and u.orgId=?2")
    List<ThridTemplate> findThridTemplateByNo(String templateNo,Long orgId);


    @Query("select u from ThridTemplate u where u.templateNo = ?1 and u.templateTypeId=?2")
    ThridTemplate listAllByNoAndTypeId(String templateNo,Long templateTypeId);
}
