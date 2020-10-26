package com.smartpc.chiyun.controller.message;

import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.controller.message.dto.MessageTemplateDTO;
import com.smartpc.chiyun.dao.dept.DepartmentDao;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.service.dept.DepartmentService;
import com.smartpc.chiyun.service.message.MessageTemplateService;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class MessageTemplateController {
    @Autowired
    DepartmentService deptService;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    UserDao userDao;

    @Autowired
    MessageTemplateService messageTemplateService;

    /**
     * 获取全部消息模板(不分页) 根据组织id区分
     * @return
     */
    @RequestMapping(value = "/messageTemplate/listAll", method = RequestMethod.GET)
    public ResultVO listAll() {
        List<MessageTemplateDTO> templates = messageTemplateService.findAllMessageTemplateByOrgId(AppUtil.getCurrentUserOrgId());
        return ResultVOUtils.success(templates);
    }


    @GetMapping(value = "/messageTemplate/id/{id}")
    public ResultVO getDepartmentById(@PathVariable Integer id) {
        MessageTemplateDTO templateDTO = messageTemplateService.findMessageTemplateById(id);
        return ResultVOUtils.success(templateDTO);
    }

    @Log("删除模板")
    @RequestMapping(value = "/messageTemplate/delete/{id}", method = RequestMethod.GET)
    public ResultVO delete(@PathVariable Integer id) {
        messageTemplateService.deleteById(id);
        return ResultVOUtils.success();
    }


    @Log("更新模板")
    @RequestMapping(value = "/messageTemplate/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody MessageTemplateDTO template) {
        if (template.getId() == null || String.valueOf(template.getId()).isEmpty()) {
            return ResultVOUtils.error(ResultEnum.UPDATEMUSTHAVEID.getStatus(), ResultEnum.UPDATEMUSTHAVEID.getMsg());
        }
        return messageTemplateService.updateMessageTemplate(template,AppUtil.getCurrentUserOrgId());

    }

    @Log("新增消息模板")
    @RequestMapping(value = "/messageTemplate/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody MessageTemplateDTO template) {
        return messageTemplateService.addMessageTemplate(template,AppUtil.getCurrentUserOrgId());
    }


}
