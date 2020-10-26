package com.smartpc.chiyun.dao.dept;


import com.smartpc.chiyun.model.dept.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentDao extends JpaRepository<Department, Long> {

    @Modifying
    @Query("delete from Department u where u.id in ?1")
    void deleteDepartmentsWithIds(List<Long> ids);


    @Query("select u from Department u where u.orgId = ?1")
    List<Department> findByOrgId(Long orgId);

    @Query("select u from Department u where u.orgId in ?1")
    Page<Department> findAll(List<Long> orgIds, Pageable pageable);

    @Query("select u from Department u where u.deptCode = ?1 and u.orgId = ?2")
    Department findDepartmentByDeptCode(String deptCode, Long orgId);

    /**
     * 父级节点为null是第一级，查询其子级
     *
     * @param
     * @return
     */
    @Query(value = "select * from ma_department  d  where  d.parent_id  is NULL and d.org_id = ?1 ", nativeQuery = true)
    List<Department> findDepartmentsByParentIdIsNull(Long orgId);

    /**
     * 根据父级节点查询子级
     *
     * @param parentId
     * @return
     */
    @Query("select d from Department d where  d.parentId = ?1 ")
    List<Department> findDepartmentByParentId(Long parentId);

    Department findDepartmentById(Long id);

    @Query("select d from Department d where  d.deptName = ?1 and d.orgId = ?2")
    Department findDepartmentByDeptNameAndOrgId(String deptName, Long orgId);

    /**
     * 根据层级查询部门
     * @param level
     * @return
     */
    @Query("select d from Department d where  d.codeLevel = ?1 ")
    List<Department> findDepartmentBylevel(Integer level);
}
