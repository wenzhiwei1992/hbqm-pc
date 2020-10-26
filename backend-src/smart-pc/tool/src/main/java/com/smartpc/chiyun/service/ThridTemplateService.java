package com.smartpc.chiyun.service;

import com.alibaba.fastjson.JSONArray;
import com.smartpc.chiyun.model.ThridTemplate;
import com.smartpc.chiyun.model.syscode.Templet;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public interface ThridTemplateService {
    ThridTemplate addThridTemplate(ThridTemplate templet);

    ThridTemplate updateThridTemplate(ThridTemplate templet);

    Page<ThridTemplate> findAll(ThridTemplate t);

    @Transactional
    void deleteThridTemplatesWithIds(List<Long> ids);

    ThridTemplate findThridTemplateById(Long templetId);

    ThridTemplate findThridTemplateByParam(String templateNo,Long templateTypeId);

    List<ThridTemplate> listAllByNo(String templateNo);

    ThridTemplate listAllByNoAndTypeId(String templateNo,Long typeId);

}
