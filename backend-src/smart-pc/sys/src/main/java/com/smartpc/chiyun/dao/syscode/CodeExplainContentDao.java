package com.smartpc.chiyun.dao.syscode;

import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zihao
 */
public interface CodeExplainContentDao extends JpaRepository<CodeExplainContent,Long>,JpaSpecificationExecutor<CodeExplainContent> {
    @Modifying
    @Query("delete from CodeExplainContent u where u.id in ?1")
    void deleteByIds(List<Long> ids);

    @Query("select e from CodeExplainContent e where e.codeNo = ?1 order by e.sort asc")
    List<CodeExplainContent> findAllByCodeNo(String codeNo);

    @Query("select e from CodeExplainContent e where e.codeNo = ?1 and e.isEnabled = 1 order by e.sort asc")
    List<CodeExplainContent> findAllByCodeNoOrderBySort(String codeNo);

    /**
     * 只针对 ProductDetailId
     * @param productDetailId
     * @return
     */
    @Query(value = "select t1.code_name from sys_dict_d t1  " +
            "inner join ( select t2.sort from pr_product_detail t1  inner join sys_dict_d t2 on t1.product_status = t2.code_name where t1.id = ?1 ) t2 \n" +
            "on t1.sort>t2.sort where t1.is_enabled = 1  "+
            "order by t1.sort limit 1 ",nativeQuery = true)
    List<String> findNextDictByDetailId(Long productDetailId);

    @Query(value = "select * from sys_dict_d t1 where t1.dict_code_no=?1 and t1.code_name=?2 and is_enabled = 1 ",nativeQuery = true)
    List<CodeExplainContent> findCodeExplainContentByCodeNoAndCodeName(String codeNo,String codeName);

    @Modifying
    @Transactional
    @Query("update CodeExplainContent u set u.remark=?1 where u.codeNo=?2 and u.codeName=?3")
    void updateCodeExplainContentRemark(String remark,String codeNo,String codeName);


    @Query(value = "select * from sys_dict_d t1 where t1.dict_code_no=?1 and t1.org_id=?2 and is_enabled = 1 order by sort desc ",nativeQuery = true)
    List<CodeExplainContent> findCodeExplainContentByCodeNoAndOrgId(String codeNo,Long orgId);

}
