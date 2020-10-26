package com.smartpc.chiyun.controller.syscode;


import com.smartpc.chiyun.service.syscode.OrderCodeRuleService;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.model.syscode.OrderRule;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zihao
 */
@RestController
public class OrderCodeRuleController {

    @Autowired
    OrderCodeRuleService orderCodeRuleService;

    @RequestMapping(value = "/basicdata/orderCodeRule/listAll", method = RequestMethod.GET)
    public ResultVO listAll() {
        return ResultVOUtils.success(orderCodeRuleService.listAll());
    }

    @RequestMapping(value = "/basicdata/orderCodeRule/getById/{id}", method = RequestMethod.GET)
    public ResultVO getById(@PathVariable Long id) {
        return ResultVOUtils.success(orderCodeRuleService.getOrderCodeRuleById(id));

    }

    @RequestMapping(value = "/basicdata/orderCodeRule/deleteOrderCodeRuleById/{id}", method = RequestMethod.GET)
    public ResultVO deleteOrderCodeRuleById(@PathVariable Long id) {
        orderCodeRuleService.deleteOrderCodeRuleById(id);
        return ResultVOUtils.success();

    }


    @RequestMapping(value = "/basicdata/orderCodeRule/getDetailByRuleId", method = RequestMethod.GET)
    public ResultVO getDetailByRuleId(Long ruleId) {
        return ResultVOUtils.success(orderCodeRuleService.getDetailByRuleId(ruleId));

    }

    @RequestMapping(value = "/basicdata/orderCodeRule/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody @Validated OrderRule codeExplain) {
        try {
            OrderRule codeExplain1 = orderCodeRuleService.findByCodeNo(codeExplain.getCodeNo());
            if (codeExplain1 != null) {
                return ResultVOUtils.error(ResultEnum.UKNOCANNOTSAME.getStatus(), ResultEnum.UKNOCANNOTSAME.getMsg());
            }
            orderCodeRuleService.add(codeExplain);
        } catch (Exception e) {
            return ResultVOUtils.error(ResultEnum.FAILED.getStatus(), ResultEnum.FAILED.getMsg());
        }
        return ResultVOUtils.success();
    }


    @RequestMapping(value = "/basicdata/orderCodeRule/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody OrderRule codeExplain) {
        OrderRule codeExplain1 = orderCodeRuleService.findByCodeNo(codeExplain.getCodeNo());
        if (codeExplain1 != null && !codeExplain1.getId().equals(codeExplain.getId())) {
            return ResultVOUtils.error(ResultEnum.UKNOCANNOTSAME.getStatus(), ResultEnum.UKNOCANNOTSAME.getMsg());
        }
        if (codeExplain.getId() == null || String.valueOf(codeExplain.getId()).isEmpty()) {
            return ResultVOUtils.error(ResultEnum.UPDATEMUSTHAVEID.getStatus(), ResultEnum.UPDATEMUSTHAVEID.getMsg());
        }
        orderCodeRuleService.update(codeExplain);
        return ResultVOUtils.success();

    }
}
