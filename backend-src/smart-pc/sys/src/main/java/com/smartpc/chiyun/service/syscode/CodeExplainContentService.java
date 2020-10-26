package com.smartpc.chiyun.service.syscode;

import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author zihao
 */
public interface CodeExplainContentService {
    Optional<CodeExplainContent> findById(Long id);

    void deleteCodeExplainContentById(Long id);

    void deleteCodeExplainContentsWithIds(List<Long> list);

    /**
     * 分页查询所有记录
     *
     * @param pageInfo
     * @return
     */
    Page<CodeExplainContent> pageableList(PageInfo pageInfo);

    void add(CodeExplainContent codeExplain);

    void update(CodeExplainContent codeExplain);

    /**
     * warning: 未分页，谨慎使用
     *
     * @return
     */
    List<CodeExplainContent> findAll();

    List<CodeExplainContent> findAllByCodeNo(String codeNo);

    /**
     * 根据字典大类编码和orgId查询
     *
     * @param codeNo
     * @return
     */
    List<CodeExplainContent> findCodeExplainContentByCodeNoAndOrgId(String codeNo);

    /**
     * 获取下一个状态
     *
     * @param currentVal
     * @param dictCodeNo
     * @return
     */
    Long getNextStatus(String currentVal, String dictCodeNo);

}
