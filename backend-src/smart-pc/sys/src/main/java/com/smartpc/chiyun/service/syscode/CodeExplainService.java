package com.smartpc.chiyun.service.syscode;

import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.model.syscode.CodeExplain;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author zihao
 */
public interface CodeExplainService {
    CodeExplain findById(Long id);

    CodeExplain getOne(Long id);

    void deleteCodeExplainById(Long id);

    void deleteCodeExplainsWithIds(List<Long> list);

    /**
     * 分页查询所有记录
     * @param pageInfo
     * @return
     */
    Page<CodeExplain> pageableList(PageInfo pageInfo);

    void add(CodeExplain codeExplain);

    void update(CodeExplain codeExplain);

    /**
     * warning: 未分页，谨慎使用
     * @return
     */
    @Deprecated
    List<CodeExplain> findAll();

    CodeExplain findByCodeNo(String codeNo);
}
