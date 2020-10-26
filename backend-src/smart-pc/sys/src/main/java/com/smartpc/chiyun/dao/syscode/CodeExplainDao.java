package com.smartpc.chiyun.dao.syscode;

import com.smartpc.chiyun.model.syscode.CodeExplain;
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
public interface  CodeExplainDao extends JpaRepository<CodeExplain,Long>,JpaSpecificationExecutor<CodeExplain> {

    @Modifying
    @Query("delete from CodeExplain u where u.id in ?1")
    void deleteCodeExplainsWithIds(List<Long> list);

    //后续可能有问题
    CodeExplain findByCodeNo(String codeNo);

    List<CodeExplain> findAllByCodeNo(String codeNo);
}
