
package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.Org;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface OrgDao extends JpaRepository<Org, Long>, JpaSpecificationExecutor<Org> {
    @Modifying
    @Query("delete from Org u where u.id in ?1")
    void deletes(List<Long> ids);

    List<Org> findByIdIn(List<Long> ids);

    @Query(value = "SELECT * FROM pc_org o WHERE o.root_org != '是'  ", nativeQuery = true)
    List<Org> findOrgsNotTrue();

    @Query("select e from Org e where e.orgId in ?1 ")
    List<Org> findAll(List<Long> orgIds);

    @Query(value = "SELECT id FROM pc_org o WHERE o.id = ?1 or o.parent_id = ?1  ", nativeQuery = true)
    List<Long> findOrgsByIdOrParentId(Long id);

    @Query(value = "SELECT o FROM Org o  ")
    List<Org> findAllOrg();

    @Query(value = "SELECT o FROM Org o where o.id = ?1 ")
    Org findOrgById(Long orgId);


    @Query("select e from Org e where e.provinceId = ?1 or e.cityId = ?1")
    List<Org> findOrgsByProvinceIdOrCityId(Long deptId);


    @Query(value = "SELECT o FROM Org o where o.proposerPhone = ?1 or o.orgName = ?2 ")
    List<Org> findOrgByProposerPhoneOrnameAndOrgName(String proposerPhone,String orgName);


    /**
     * 插入初四审批记录
     */
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO pc_approval ( createdBy, createdByName, dateCreated, dateModified, modifiedBy, modifiedByName, org_id, approval_level, approval_status, association_id, city_id, code_type, province_id, reason, approval_dept_id) " +
            "VALUES ( ?1, ?2, ?3, ?4, ?5, ?6,?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15) ", nativeQuery = true)
    void insertApproval(String  createdBy,
                        String createdByName,
                        Date dateCreated,
                        Date dateModified,
                        String modifiedBy,
                        String modifiedByName,
                        Long org_id,
                        String approval_level,
                        String approval_status,
                        Long association_id,
                        Long city_id,
                        String code_type,
                        Long province_id,
                        String reason,
                        Long approval_dept_id );

}
