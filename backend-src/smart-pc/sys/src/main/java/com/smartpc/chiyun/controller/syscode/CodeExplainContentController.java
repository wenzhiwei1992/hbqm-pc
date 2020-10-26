package com.smartpc.chiyun.controller.syscode;


import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.service.syscode.CodeExplainContentService;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import com.smartpc.chiyun.service.syscode.dto.SysCodeDto;
import com.smartpc.chiyun.utils.ClassUtil;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author zihao
 */
@RestController
public class CodeExplainContentController {

    @Autowired
    CodeExplainContentService codeExplainContentService;


    /**
     * 根据系统编码分类ID查询编码集合
     *
     * @param codeNo
     * @return
     */
    @GetMapping("/basicdata/codeExplainContent/getByCodeNo/{codeNo}")
    public ResultVO findAllByCodeNo(@PathVariable String codeNo) {
        List<CodeExplainContent> list = codeExplainContentService.findAllByCodeNo(codeNo);
        return ResultVOUtils.success(list);
    }


    /**
     * 获取系统编码列表
     *
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = "/basicdata/codeExplainContent/list", method = RequestMethod.POST)
    public ResultVO list(@RequestBody PageInfo pageInfo) {
        if (pageInfo.getPageSize() <= 0 || pageInfo.getCurrentPage() <= 0) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ResultEnum.PAGEINFODATAERR.getMsg(), "");
        }
        Page<CodeExplainContent> codeExplainContentLists = codeExplainContentService.pageableList(pageInfo);

        return ResultVOUtils.success(codeExplainContentLists);
    }


    @GetMapping(value = "/basicdata/codeExplainContent/getById/{id}")
    public ResultVO getCodeExplainContentById(@PathVariable Long id) {
        ResultVO resultVO = new ResultVO();
        Optional<CodeExplainContent> op = codeExplainContentService.findById(id);
        op.ifPresent(new Consumer<CodeExplainContent>() {
            @Override
            public void accept(CodeExplainContent codeExplainContent) {
                if (codeExplainContent == null) {
                    resultVO.setStatus(ResultEnum.GETONEFAILED.getStatus());
                    resultVO.setMsg(ResultEnum.GETONEFAILED.getMsg());
                }
                resultVO.setStatus("success");
                resultVO.setEntity(codeExplainContent);
            }
        });
        return resultVO;
    }


    @RequestMapping(value = "/basicdata/codeExplainContent/delete/{id}", method = RequestMethod.DELETE)
    public ResultVO delete(@PathVariable Long id) {
        codeExplainContentService.deleteCodeExplainContentById(id);
        return ResultVOUtils.success();
    }

    @RequestMapping(value = "/basicdata/codeExplainContent/deleteByIds", method = RequestMethod.DELETE)
    public ResultVO deleteByIds(String ids) {
        String[] split = ids.split(",");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        codeExplainContentService.deleteCodeExplainContentsWithIds(list);
        return ResultVOUtils.success();
    }

    @RequestMapping(value = "/basicdata/codeExplainContent/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody CodeExplainContent CodeExplainContent) {
        if (CodeExplainContent.getId() == null || String.valueOf(CodeExplainContent.getId()).isEmpty()) {
            return ResultVOUtils.error(ResultEnum.UPDATEMUSTHAVEID.getStatus(), ResultEnum.UPDATEMUSTHAVEID.getMsg());
        }
        codeExplainContentService.update(CodeExplainContent);
        return ResultVOUtils.success();

    }

    @RequestMapping(value = "/basicdata/codeExplainContent/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody @Validated CodeExplainContent CodeExplainContent) {
        try {
            codeExplainContentService.add(CodeExplainContent);
        } catch (Exception e) {
            return ResultVOUtils.error(ResultEnum.FAILED.getStatus(), ResultEnum.FAILED.getMsg());
        }
        return ResultVOUtils.success();
    }

    @ApiOperation("根据字典大类编码查询字典详情")
    @PostMapping("/basicdata/codeExplainContent/findCodeByCodeNoAndOrgId")
    public ResultVO findCodeByCodeNoAndOrgId(@RequestBody SysCodeDto sysCodeDto) {
        List<CodeExplainContent> list = codeExplainContentService.findCodeExplainContentByCodeNoAndOrgId(sysCodeDto.getCodeNo());
        return ResultVOUtils.success(list);
    }
}
