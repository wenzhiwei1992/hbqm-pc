package com.smartpc.chiyun.service.syscode;

import com.alibaba.fastjson.JSONArray;
import com.smartpc.chiyun.model.syscode.Templet;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author yue
 * @create 2020/2/19 4:27 下午
 */
public interface TempletService {

    Templet addTemplet(Templet templet);

    Templet updateTemplet(Templet templet);

    void deleteTemplet(Long templetId);

    List<Templet> findTemplets();

    Page<Templet> findAll(Templet t);

    @Transactional
    void deleteTempletsWithIds(List<Long> ids);

    Templet findTempletById(Long templetId);

    JSONArray findTempletByTempletNo(String templetNo);
}
