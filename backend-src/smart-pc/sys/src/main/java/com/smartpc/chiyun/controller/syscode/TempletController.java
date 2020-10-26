package com.smartpc.chiyun.controller.syscode;


import com.alibaba.fastjson.JSONArray;
import com.smartpc.chiyun.service.syscode.TempletService;
import com.smartpc.chiyun.aop.Log;

import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.syscode.Templet;


import com.smartpc.chiyun.model.sys.SR;

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
 * @Author yue
 * @create 2020/2/19 11:23 下午
 */
@Slf4j
@RestController
@Api(tags = "系统模版相关接口")
public class TempletController {

    @Autowired
    TempletService templetService;

    @Log("删除模版")
    @RequestMapping(value = "/basicdata/templet/deletes", method = RequestMethod.DELETE)
    public SR<Templet> delete(String ids) {
        String[] split = ids.split(";");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        SR sr = new SR();
        try {
            templetService.deleteTempletsWithIds(list);
        } catch (Exception e) {
            sr.setMsg("已选信息中已有其他关联信息,不能删除！");
            return sr;
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("查询模版")
    @RequestMapping(value = "/basicdata/templet/list", method = RequestMethod.POST)
    public SR<Page<Templet>> list(@RequestBody Templet templet) {
        SR<Page<Templet>> sr = new SR<>();
        Page<Templet> pageableList = templetService.findAll(templet);
        sr.setEntity(pageableList);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("更新模版")
    @RequestMapping(value = "/basicdata/templet/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody Templet templet) {
        templetService.updateTemplet(templet);
        return ResultVOUtils.success(templet);
    }

    @Log("新增模版")
    @RequestMapping(value = "/basicdata/templet/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody Templet templet) {
        templetService.addTemplet(templet);
        return ResultVOUtils.success(templet);

    }

    @GetMapping("/basicdata/templet/findTemoletById/{templetId}")
    public ResultVO findTempletById(@PathVariable Long templetId) {
        Templet templet = templetService.findTempletById(templetId);
        return ResultVOUtils.success(templet);
    }

    @ApiOperation("通过编号查询系统模版接口")
    @GetMapping("/basicdata/templet/findTempletByTempletNo/{templetNo}")
    public ResultVO findTempletByTempletNo(@PathVariable String templetNo) {
        JSONArray jsonObject = new JSONArray();
        try {
            jsonObject = templetService.findTempletByTempletNo(templetNo);
        } catch (DataNotFoundException e) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), e.getMsg());
        }
        return ResultVOUtils.success(jsonObject);
    }
}
