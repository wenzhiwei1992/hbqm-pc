package com.smartpc.chiyun.controller.syscode;

import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.model.syscode.CodeExplain;
import com.smartpc.chiyun.service.syscode.CodeExplainService;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zihao
 */
@RestController
public class CodeExplainController {

    @Autowired
    CodeExplainService codeExplainService;

    /**
     * 获取系统编码列表
     *
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = "/basicdata/codeExplain/list", method = RequestMethod.POST)
    public ResultVO list(@RequestBody PageInfo pageInfo) {
        if (pageInfo.getPageSize() <= 0 || pageInfo.getCurrentPage() <= 0) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ResultEnum.PAGEINFODATAERR.getMsg(), "");
        }
        Page<CodeExplain> codeExplainLists = codeExplainService.pageableList(pageInfo);

        return ResultVOUtils.success(codeExplainLists);
    }

    @GetMapping(value = "/basicdata/codeExplain/getById/{id}")
    public ResultVO getCodeExplainById1(@PathVariable Long id) {
         CodeExplain op = codeExplainService.findById(id);
        return ResultVOUtils.success(op);
    }

    @GetMapping(value = "/basicdata/codeExplain/getCodeExplainOne/{id}")
    public ResultVO getCodeExplainOne(@PathVariable Long id) {
        CodeExplain codeExplain = codeExplainService.getOne(id);
        return ResultVOUtils.success(codeExplain);
    }

    @RequestMapping(value = "/basicdata/codeExplain/delete/{id}", method = RequestMethod.DELETE)
    public ResultVO delete(@PathVariable Long id) {
        codeExplainService.deleteCodeExplainById(id);
        return ResultVOUtils.success();
    }

    @RequestMapping(value = "/basicdata/codeExplain/deleteByIds", method = RequestMethod.DELETE)
    public ResultVO deleteByIds(String ids) {
        String[] split = ids.split(",");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        codeExplainService.deleteCodeExplainsWithIds(list);
        return ResultVOUtils.success();
    }

    @RequestMapping(value = "/basicdata/codeExplain/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody CodeExplain codeExplain) {
        if (codeExplain.getId() == null || String.valueOf(codeExplain.getId()).isEmpty()) {
            return ResultVOUtils.error(ResultEnum.UPDATEMUSTHAVEID.getStatus(), ResultEnum.UPDATEMUSTHAVEID.getMsg());
        }
        codeExplainService.update(codeExplain);
        return ResultVOUtils.success();

    }

    @RequestMapping(value = "/basicdata/codeExplain/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody @Validated CodeExplain codeExplain) {
        try {
            CodeExplain codeExplain1 =codeExplainService.findByCodeNo(codeExplain.getCodeNo());
            if(codeExplain1!=null){
                return ResultVOUtils.error(ResultEnum.UKNOCANNOTSAME.getStatus(), ResultEnum.UKNOCANNOTSAME.getMsg());
            }
            codeExplainService.add(codeExplain);
        } catch (Exception e) {
            return ResultVOUtils.error(ResultEnum.FAILED.getStatus(), ResultEnum.FAILED.getMsg());
        }
        return ResultVOUtils.success();
    }

}
