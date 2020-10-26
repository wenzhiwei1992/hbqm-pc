package com.smartpc.chiyun.service.syscode;


import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.dao.syscode.CodeExplainContentDao;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author zihao
 */
@Service
public class CodeExplainContentServiceImpl implements CodeExplainContentService {
    @Autowired
    CodeExplainContentDao codeExplainContentDao;


    @Override
    public Optional<CodeExplainContent> findById(Long id) {
        return codeExplainContentDao.findById(id);
    }


    @Override
    public void deleteCodeExplainContentById(Long id) {
        codeExplainContentDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteCodeExplainContentsWithIds(List<Long> list) {
        codeExplainContentDao.deleteByIds(list);
    }

    /**
     * 分页查询所有记录
     *
     * @param pageInfo
     * @return
     */
    @Override
    public Page<CodeExplainContent> pageableList(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getCurrentPage() - 1, pageInfo.getPageSize(), new Sort(Sort.Direction.DESC, "dateModified"));

        return codeExplainContentDao.findAll(pageable);
    }

    @Override
    public void add(CodeExplainContent codeExplainContent) {
        codeExplainContentDao.save(codeExplainContent);
    }

    @Override
    public void update(CodeExplainContent codeExplainContent) {
        codeExplainContentDao.save(codeExplainContent);
    }

    /**
     * warning: 未分页，谨慎使用
     *
     * @return
     */
    @Override
    public List<CodeExplainContent> findAll() {
        return codeExplainContentDao.findAll();
    }

    @Override
    public List<CodeExplainContent> findAllByCodeNo(String codeNo) {
        List<CodeExplainContent> list = codeExplainContentDao.findAllByCodeNo(codeNo);
        return list;
    }

    @Override
    public List<CodeExplainContent> findCodeExplainContentByCodeNoAndOrgId(String codeNo) {
        Long orgId = AppUtil.getCurrentUserOrgId();
        return codeExplainContentDao.findCodeExplainContentByCodeNoAndOrgId(codeNo, orgId);
    }

    @Override
    public Long getNextStatus(String currentVal, String dictCodeNo) {
        //已经排好序
        List<CodeExplainContent> dicts = codeExplainContentDao.findAllByCodeNoOrderBySort(dictCodeNo);
        //dicts.sort(Comparator.comparing(CodeExplainContent::getSort));
        if (dicts.size() == 0) {
            return null;
        }

        if (currentVal.equalsIgnoreCase("first")) {
            return dicts.get(0).getId();
        }

        Long nextStatus = null;
        int res = -1;
        //排序后,值相等的下一个元素就是下个状态
        for (int i = 0; i < dicts.size(); i++) {
            CodeExplainContent content = dicts.get(i);
            if (content.getCodeName().equals(currentVal)) {
                // res 为当前下标的下一个
                res = i + 1;
                break;
            }
        }
        if (res != -1) {
            CodeExplainContent content = dicts.get(res);
            nextStatus = content.getId();
        }
        return nextStatus;
    }

}
