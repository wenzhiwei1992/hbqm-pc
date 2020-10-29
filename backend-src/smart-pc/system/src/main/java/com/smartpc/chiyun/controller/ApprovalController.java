package com.smartpc.chiyun.controller;

import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.dao.ApprovalDao;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.model.Approval;
import com.smartpc.chiyun.service.ApprovalService;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/approval")
@RestController
@Slf4j
public class ApprovalController {

    @Autowired
    ApprovalService approvalService;

    @Autowired
    ApprovalDao approvalDao;

    @Log("查询审批记录")
    @RequestMapping(value = "/findByTypeAndAssociationId", method = RequestMethod.POST)
    public ResultVO findByTypeAndAssociationId(@RequestBody Approval entity) {
        try{
            return ResultVOUtils.success(approvalService.getApprovalVOInfo(entity));
        }catch (Exception e){
            log.error("查询审批详情失败：entity："+ JSONObject.toJSONString(entity),e);
        }
      return ResultVOUtils.error(ResultEnum.DATASIZEISZORE);
    }


    @Log("新增审批")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody Approval entity) {
        String  msg = approvalService.saveApproval(entity);
        if("success".equals(msg)){
            return ResultVOUtils.success();
        }else{
            return ResultVOUtils.error("fail",msg);
        }
    }

}
