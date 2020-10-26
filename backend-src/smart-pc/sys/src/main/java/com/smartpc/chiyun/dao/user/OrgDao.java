
package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.Org;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

}
