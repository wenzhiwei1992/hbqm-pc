package com.smartpc.chiyun.service.syscode;

import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.dao.syscode.CodeExplainContentDao;
import com.smartpc.chiyun.dao.syscode.CodeExplainDao;
import com.smartpc.chiyun.model.syscode.CodeExplain;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zihao
 */

@Service
public class CodeExplainServiceImpl implements CodeExplainService {

    @Autowired
    CodeExplainDao codeExplainDao;

    @Autowired
    private CodeExplainContentDao codeExplainContentDao;

    @Override
    public CodeExplain findById(Long id) {
        CodeExplain codeExplain = codeExplainDao.findById(id).get();
        List<CodeExplainContent> list = codeExplainContentDao.findAllByCodeNo(codeExplain.getCodeNo());
        codeExplain.setCodeContents(list);
        return codeExplain;
    }

    @Override
    public CodeExplain getOne(Long id) {
        return codeExplainDao.getOne(id);
    }

    @Override
    public void deleteCodeExplainById(Long id) {
        CodeExplain codeExplain = codeExplainDao.findById(id).get();

        List<CodeExplainContent> allByCodeNo = codeExplainContentDao.findAllByCodeNo(codeExplain.getCodeNo());
        //删除代码子集
        codeExplainContentDao.deleteAll(allByCodeNo);
        //删除代码
        codeExplainDao.deleteById(id);

    }

    @Transactional
    @Override
    @Deprecated
    public void deleteCodeExplainsWithIds(List<Long> list) {
        codeExplainDao.deleteCodeExplainsWithIds(list);
    }

    /**
     * 分页查询所有记录
     *
     * @param pageInfo
     * @return
     */
    @Override
    public Page<CodeExplain> pageableList(PageInfo pageInfo) {
        Pageable pageable= PageRequest.of(pageInfo.getCurrentPage()-1,pageInfo.getPageSize(),new Sort(Sort.Direction.DESC, "dateModified"));

        return codeExplainDao.findAll(pageable);
    }

    @Transactional(rollbackForClassName = "Exception")
    @Override
    public void add(CodeExplain codeExplain) {

        List<CodeExplainContent> codeContents = codeExplain.getCodeContents();
        codeExplainDao.save(codeExplain);
        codeExplainContentDao.saveAll(codeContents);
    }

    @Transactional(rollbackForClassName = "Exception")
    @Override
    public void update(CodeExplain codeExplain) {
        List<CodeExplainContent> allByCodeNo = codeExplainContentDao.findAllByCodeNo(codeExplain.getCodeNo());
        //先删除代码子集
        codeExplainContentDao.deleteAll(allByCodeNo);

        List<CodeExplainContent> codeContents = codeExplain.getCodeContents();
        codeExplainDao.save(codeExplain);
        codeExplainContentDao.saveAll(codeContents);
    }

    /**
     * warning: 未分页，谨慎使用
     *
     * @return
     */
    @Override
    public List<CodeExplain> findAll() {
        return codeExplainDao.findAll();
    }

    @Override
    public CodeExplain findByCodeNo(String codeNo) {
        return codeExplainDao.findByCodeNo(codeNo);
    }
}
