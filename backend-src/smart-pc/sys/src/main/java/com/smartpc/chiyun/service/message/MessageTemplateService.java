package com.smartpc.chiyun.service.message;

import com.alibaba.fastjson.JSON;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.controller.message.dto.MessageTemplateDTO;
import com.smartpc.chiyun.dao.message.MessageTemplateDao;
import com.smartpc.chiyun.dao.quartz.QuartzJobDao;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.model.message.MessageTemplate;
import com.smartpc.chiyun.model.quartz.QuartzJob;
import com.smartpc.chiyun.utils.CloneUtils;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import com.smartpc.chiyun.model.user.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageTemplateService {
    @Autowired
    UserDao userDao;

    @Autowired
    MessageTemplateDao messageTemplateDao;

    @Autowired
    QuartzJobDao quartzJobDao;

    public MessageTemplateDTO findMessageTemplateById(Integer id) {
        Optional<MessageTemplate> op = messageTemplateDao.findById(id);
        MessageTemplateDTO dto = new MessageTemplateDTO();
        if (op.isPresent()) {
            BeanUtils.copyProperties(op.get(), dto);
            dto.setReceiverTypeName(getReceiverTypeName(dto.getReceiverType()));
            dto.setReceiverName(getReceiverName(dto.getReceiverId()));
        }
        return dto;
    }


    public void deleteById(Integer id) {
        messageTemplateDao.deleteById(id);
    }

    public String getReceiverTypeName(int typeId){
        String receiverTypeName = "";
        switch(typeId){
            case 0:
                receiverTypeName =  "用户";
                break;
            case 1:
                receiverTypeName =  "部门";
                break;
            case 2:
                receiverTypeName =  "角色";
                break;
        }
        return receiverTypeName;
    }


    public String getReceiverName(String receiverId){
        String receiverName = "";
        if(StringUtil.isNotNullAndEmpty(receiverId)){
            List<User> users = userDao.findByIdIn(StringUtil.splitToLong(receiverId,","));
            if(!users.isEmpty()){
                StringBuffer sb = new StringBuffer();
                for(User user : users){
                    sb.append(user.getRealName()).append(",");
                }
                return sb.deleteCharAt(sb.length() - 1).toString();
            }

        }
        return receiverName;
    }

    public List<MessageTemplateDTO> findAllMessageTemplateByOrgId(Long orgId) {
        List<MessageTemplate> list =  messageTemplateDao.findAllByOrgId(orgId);
        List<MessageTemplateDTO> listDto =
                JSON.parseArray(JSON.toJSONString(list), MessageTemplateDTO.class);

        for(MessageTemplateDTO dto : listDto){
            dto.setReceiverTypeName(getReceiverTypeName(dto.getReceiverType()));
            dto.setReceiverName(getReceiverName(dto.getReceiverId()));
            QuartzJob quartzJob = quartzJobDao.findByJobName(dto.getTypeCode(), AppUtil.getCurrentUserOrgId());
            if(quartzJob!=null){
                dto.setCron(quartzJob.getCron());
            }
        }
        return listDto;
    }


    public MessageTemplate  findMessageTemplateByTypeCodeAndOrgId(String typeCode,Long orgId) {
        return messageTemplateDao.findMessageTemplateByTypeCodeAndOrgId(typeCode,orgId);
    }



    public ResultVO updateMessageTemplate(MessageTemplateDTO templateDTO, Long orgId){
        //判断该code是否已经存在
        MessageTemplate exitsInfo = findMessageTemplateByTypeCodeAndOrgId(templateDTO.getTypeCode(), orgId);
        if(exitsInfo!=null && !exitsInfo.getId().equals(templateDTO.getId())){
            return  ResultVOUtils.error(ResultEnum.CODE_EXISTS.getStatus(),"该部门代码已经存在");
        }

        MessageTemplate template = messageTemplateDao.getOne(templateDTO.getId());
        CloneUtils.copyPropertiesIgnoreNull(templateDTO,template);
        messageTemplateDao.save(template);

        return ResultVOUtils.success();
    }


    public ResultVO addMessageTemplate(MessageTemplateDTO templateDTO,Long orgId) {
        //判断该code是否已经存在
        MessageTemplate exitsInfo = findMessageTemplateByTypeCodeAndOrgId(templateDTO.getTypeCode(), orgId);
        if(exitsInfo!=null){
            return  ResultVOUtils.error(ResultEnum.CODE_EXISTS.getStatus(),"该部门代码们已经存在");
        }

        MessageTemplate template = new MessageTemplate();
        BeanUtils.copyProperties(templateDTO, template);
        messageTemplateDao.save(template);
        return ResultVOUtils.success();
    }



}
