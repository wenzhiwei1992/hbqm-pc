package com.smartpc.chiyun.service.impl;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.ThridTemplateDao;
import com.smartpc.chiyun.model.ThridTemplate;
import com.smartpc.chiyun.service.ThridTemplateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
public class ThridTemplateServiceImpl implements ThridTemplateService {
    @Autowired
    ThridTemplateDao thridTemplateDao;

    @Override
    public ThridTemplate addThridTemplate(ThridTemplate templet) {
        return thridTemplateDao.save(templet);
    }

    @Override
    public ThridTemplate updateThridTemplate(ThridTemplate templet) {
        return thridTemplateDao.save(templet);
    }


    @Override
    public Page<ThridTemplate> findAll(ThridTemplate t) {
        Pageable pageable = PageRequest.of(t.getPageInfo().getCurrentPage() - 1, t.getPageInfo().getPageSize(), new Sort(Sort.Direction.DESC, "dateModified"));
        Page<ThridTemplate> tList = thridTemplateDao.findAll(new Specification<ThridTemplate>() {
            @Override
            public Predicate toPredicate(Root<ThridTemplate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("orgId"), AppUtil.getCurrentUserOrgId()));
                Predicate[] pre = new Predicate[predicates.size()];
                criteriaQuery.where(predicates.toArray(pre));
                return criteriaBuilder.and(predicates.toArray(pre));
            }
        }, pageable);

        return tList;
    }

    @Override
    public void deleteThridTemplatesWithIds(List<Long> ids) {
        thridTemplateDao.deleteThridTemplatesWithIds(ids);
    }

    @Override
    public ThridTemplate findThridTemplateById(Long templetId) {
        return thridTemplateDao.findById(templetId).get();
    }

    @Override
    public ThridTemplate findThridTemplateByParam(String templateNo,Long templateTypeId){
        return thridTemplateDao.findThridTemplateByParam(templateNo,templateTypeId);
    }


    @Override
    public List<ThridTemplate> listAllByNo(String templateNo){
        return thridTemplateDao.findThridTemplateByNo(templateNo, AppUtil.getCurrentUserOrgId());
    }

    @Override
    public ThridTemplate listAllByNoAndTypeId(String templateNo,Long typeId){
        return thridTemplateDao.listAllByNoAndTypeId(templateNo, typeId);
    }
   }
