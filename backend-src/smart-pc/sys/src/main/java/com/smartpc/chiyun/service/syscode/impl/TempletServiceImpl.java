package com.smartpc.chiyun.service.syscode.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.dao.syscode.TempletDao;
import com.smartpc.chiyun.service.syscode.TempletService;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.syscode.Templet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yue
 * @create 2020/2/19 4:28 下午
 */
@Service
public class TempletServiceImpl implements TempletService {
    @Autowired
    TempletDao templetDao;

    @Override
    public Templet addTemplet(Templet templet) {
        return templetDao.save(templet);
    }

    @Override
    public Templet updateTemplet(Templet templet) {
        return templetDao.save(templet);
    }

    @Override
    public void deleteTemplet(Long templetId) {

    }

    @Override
    public List<Templet> findTemplets() {
        return templetDao.findAll();
    }

    @Override
    public Page<Templet> findAll(Templet t) {
        Pageable pageable = PageRequest.of(t.getPageInfo().getCurrentPage() - 1, t.getPageInfo().getPageSize(), new Sort(Sort.Direction.DESC, "dateModified"));
        Page<Templet> tList = templetDao.findAll(new Specification<Templet>() {
            @Override
            public Predicate toPredicate(Root<Templet> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                Predicate[] pre = new Predicate[predicates.size()];
                criteriaQuery.where(predicates.toArray(pre));
                return criteriaBuilder.and(predicates.toArray(pre));
            }
        }, pageable);
        return tList;
    }

    @Override
    public void deleteTempletsWithIds(List<Long> ids) {
        templetDao.deleteTempletsWithIds(ids);
    }

    @Override
    public Templet findTempletById(Long templetId) {
        return templetDao.findTempletById(templetId);
    }

    @Override
    public JSONArray findTempletByTempletNo(String templetNo) {
        List<Templet> templetList = templetDao.findTempletByTempletNo(templetNo);
        if (templetList.size() > 1) {
            throw new DataNotFoundException("模版编号重复");
        }
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = JSONObject.parseArray(templetList.get(0).getTempletJson());
        } catch (Exception e) {
            throw new DataNotFoundException("请检查对应系统模版");
        }
        return jsonArray;

    }
}
