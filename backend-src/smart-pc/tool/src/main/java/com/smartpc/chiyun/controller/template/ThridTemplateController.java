package com.smartpc.chiyun.controller.template;


import com.alibaba.fastjson.JSONArray;
import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.ThridTemplate;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.syscode.Templet;
import com.smartpc.chiyun.service.ThridTemplateService;
import com.smartpc.chiyun.service.syscode.TempletService;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lee
 * @create 2020年8月11日15:49:11 下午
 */
@Slf4j
@RestController
@Api(tags = "三方模版相关接口")
public class ThridTemplateController {

    @Autowired
    ThridTemplateService thridTemplateService;

    @Log("删除模版")
    @RequestMapping(value = "/thridTemplate/deletes", method = RequestMethod.DELETE)
    public SR<Templet> delete(String ids) {
        String[] split = ids.split(";");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        SR sr = new SR();
        try {
            thridTemplateService.deleteThridTemplatesWithIds(list);
        } catch (Exception e) {
            sr.setMsg("已选信息中已有其他关联信息,不能删除！");
            return sr;
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("查询模版")
    @RequestMapping(value = "/thridTemplate/list", method = RequestMethod.POST)
    public SR<Page<ThridTemplate>> list(@RequestBody ThridTemplate templet) {
        SR<Page<ThridTemplate>> sr = new SR<>();
        Page<ThridTemplate> pageableList = thridTemplateService.findAll(templet);
        sr.setEntity(pageableList);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("更新模版")
    @RequestMapping(value = "/thridTemplate/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody ThridTemplate templet) {
        //查重
        ThridTemplate info = thridTemplateService.findThridTemplateByParam(templet.getTemplateNo(), templet.getTemplateTypeId());

        if (info != null  && !templet.getId().equals(info.getId())) {
            return ResultVOUtils.error("failed","该类型信息已存在");
        }
        thridTemplateService.updateThridTemplate(templet);

        return ResultVOUtils.success(templet);
    }

    @Log("新增模版")
    @RequestMapping(value = "/thridTemplate/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody ThridTemplate templet) {
        //查重
        ThridTemplate info = thridTemplateService.findThridTemplateByParam(templet.getTemplateNo(), templet.getTemplateTypeId());

        if (info != null) {
            return ResultVOUtils.error("failed","该类型信息已存在");
        }
        thridTemplateService.addThridTemplate(templet);
        return ResultVOUtils.success(templet);

    }

    @GetMapping("/thridTemplate/findThridTemplateById/{templetId}")
    public ResultVO findTempletById(@PathVariable Long templetId) {
        ThridTemplate templet = thridTemplateService.findThridTemplateById(templetId);
        return ResultVOUtils.success(templet);
    }


    @Log("查询模版")
    @RequestMapping(value = "/thridTemplate/listAllByNo", method = RequestMethod.GET)
    public ResultVO listAllByNo(String templateNo) {
        return ResultVOUtils.success(thridTemplateService.listAllByNo(templateNo));
    }

    @Log("查询模版")
    @RequestMapping(value = "/thridTemplate/listAllByNoAndTypeId", method = RequestMethod.GET)
    public ResultVO listAllByNoAndTypeId(String templateNo, Long typeId) {
        return ResultVOUtils.success(thridTemplateService.listAllByNoAndTypeId(templateNo, typeId));
    }

}
