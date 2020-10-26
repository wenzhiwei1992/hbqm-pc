package com.smartpc.chiyun.service.dept;

import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.model.dept.Department;
import com.smartpc.chiyun.model.user.vo.ProductTypeHandleVO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    void deleteDeptById(Long id);

    void deleteDeptsWithIds(List<Long> list);

    /**
     * 分页查询所有记录
     *
     * @param pageInfo
     * @return
     */
    Page<Department> pageableList(PageInfo pageInfo);

    void add(Department dept);

    void update(Department dept);

    Optional<Department> getOne(Long id);

    List<Department> findAll();

    List<Department> findByOrgId();

    /**
     * 树结构存储构件类型
     *
     * @return
     */
    List<Object> getDepartmentsToTree(Long id);

    /**
     * 根据父级节点查询子级
     *
     * @param parentId
     * @returnv
     */
    List<Department> findDepartmentByParentId(Long parentId, Long orgId);

    Department findDepartmentById(Long id);

    List<Department> findDepartmentBylevel(Integer level);


    /**
     * 拖拽更新节点
     *
     * @param productTypeHandleVO
     */
    Long updateDeptCodeLevelByDrag(ProductTypeHandleVO productTypeHandleVO);

    /**
     * 根据名称查询部门信息
     *
     * @param deptName
     * @return
     */
    Department findDepartmentByDeptName(String deptName);


}
