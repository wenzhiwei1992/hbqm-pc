package com.smartpc.chiyun.service.syscode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.dao.syscode.OrderCodeRuleDao;
import com.smartpc.chiyun.dao.syscode.OrderCodeRuleDetailDao;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.http.PostDataTools;
import com.smartpc.chiyun.model.syscode.OrderRule;
import com.smartpc.chiyun.model.syscode.OrderRuleDetail;
import com.smartpc.chiyun.service.syscode.dto.OrderCodeInvoiceDto;
import com.smartpc.chiyun.service.syscode.dto.OrderCodeMaDto;
import com.smartpc.chiyun.service.syscode.dto.OrderCodeRecodeDto;
import com.smartpc.chiyun.utils.RequestHolder;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zihao
 */
@Slf4j
@Service
public class OrderCodeRuleServiceImpl implements OrderCodeRuleService {

    @Autowired
    OrderCodeRuleDao orderCodeRuleDao;

    @Autowired
    private OrderCodeRuleDetailDao orderCodeRuleDetailDao;


    @Override
    public List<OrderRule> listAll() {
        return orderCodeRuleDao.findByEndTimeIsNull();
    }

    @Override
    public OrderRule getOrderCodeRuleById(Long id) {
        OrderRule orderRule = orderCodeRuleDao.getOrderCodeRuleById(id);
        orderRule.setCodeContents(orderCodeRuleDetailDao.findAllByRuleId(id));
        return orderRule;
    }

    @Override
    public void deleteOrderCodeRuleById(Long id) {
        OrderRule orderRule = orderCodeRuleDao.getOrderCodeRuleById(id);
        //再更新父级endTim
        orderRule.setEndTime(new Date());
        orderCodeRuleDao.save(orderRule);
        List<OrderRuleDetail> allByCodeNo = orderCodeRuleDetailDao.findAllByRuleId(orderRule.getId());
        //删除代码子集
        orderCodeRuleDetailDao.deleteAll(allByCodeNo);
    }


    @Override
    public List<OrderRuleDetail> getDetailByRuleId(Long ruleId) {
        return orderCodeRuleDetailDao.findAllByRuleId(ruleId);
    }

    @Transactional(rollbackForClassName = "Exception")
    @Override
    public void add(OrderRule orderRule) {
        orderCodeRuleDao.save(orderRule);
        List<OrderRuleDetail> codeContents = orderRule.getCodeContents().stream().map(e -> {
            e.setRuleId(orderRule.getId());
            return e;
        }).collect(Collectors.toList());
        orderCodeRuleDetailDao.saveAll(codeContents);
    }

    @Transactional(rollbackForClassName = "Exception")
    @Override
    public void update(OrderRule orderRule) {

        List<OrderRuleDetail> allByCodeNo = orderCodeRuleDetailDao.findAllByRuleId(orderRule.getId());
        //先删除代码子集
        orderCodeRuleDetailDao.deleteAll(allByCodeNo);

        //再更新父级endTim
        orderRule.setEndTime(new Date());
        orderCodeRuleDao.save(orderRule);

        //新增
        OrderRule rule1 = new OrderRule();
        BeanUtils.copyProperties(orderRule, rule1);
        rule1.setId(null);
        rule1.setEndTime(null);
        orderCodeRuleDao.save(rule1);
        List<OrderRuleDetail> codeContents = orderRule.getCodeContents().stream().map(e -> {
            e.setRuleId(rule1.getId());
            return e;
        }).collect(Collectors.toList());
        orderCodeRuleDetailDao.saveAll(codeContents);
    }

    @Override
    public OrderRule findByCodeNo(String code) {
        return orderCodeRuleDao.findByCodeNoAndEndTimeIsNull(code);
    }


    @Override
    public String getOrderNoByCode(OrderRule orderRule) {
        String code = orderRule.getCodeNo();
        String orderNo = "";
        if (orderRule == null) {
            return orderNo;
        }

        List<OrderRuleDetail> allByCodeNo = orderCodeRuleDetailDao.findAllByRuleId(orderRule.getId());
        //循环判断规则类型

        String str1 = "";
        String str2 = "%";
        Boolean flag = false; //是否存在流水码
        int pipelinedNum = 0;//流水码位数
        for (OrderRuleDetail orderRuleDetail : allByCodeNo) {

            if (orderRuleDetail.getDictCodeName().indexOf("时间") != -1) {//时间类型字符处理时间
                SimpleDateFormat sdf = new SimpleDateFormat(orderRuleDetail.getRemark());
                orderNo += sdf.format(new Date());
                if (flag) {
                    str2 += sdf.format(new Date());
                } else {
                    str1 += sdf.format(new Date());
                }

            } else if (orderRuleDetail.getDictCodeName().indexOf("流水") != -1) {//流水号字符生成流水码
                pipelinedNum = Integer.parseInt(orderRuleDetail.getRemark());
                String pipelinedRule = "%0" + orderRuleDetail.getRemark() + "d";
                // 不一定是1 这里要特殊处理
                //这里考虑过后 选择每次去库里读取  因为规则中流水码是随着时间重置的
                String number = String.format(pipelinedRule, 1);
                //
                orderNo += number;
            } else {//普通字符直接拼接
                orderNo += orderRuleDetail.getRemark();
                if (flag) {
                    str2 += orderRuleDetail.getRemark();
                } else {
                    str1 += orderRuleDetail.getRemark();
                }
            }
        }
        str1 += "%";
        int length = orderNo.length();
        int temp = 0;
        int num = 0;
        List<String> nos = new ArrayList<>();
        //去各个单号对应的表中寻找生成的单号数据
        //获取IP地址
        String ip = RequestHolder.getIpAndPort();
        if ("SLD".equals(code) || "BD".equals(code) || "PKBC".equals(code)) {//收料单磅单盘库补差
            OrderCodeMaDto orderCodeMaDto = new OrderCodeMaDto();
            orderCodeMaDto.setCodeNoOne(str1);
            orderCodeMaDto.setCodeNoTwo(str2);
            orderCodeMaDto.setLength(length);
            //2020-09-07 18：10 注释 单体架构限制，模块不能拆出，直接通过https请求，并且还是外网，会导致出现问题无法排查，因此暂时不用
           /* PostDataTools post = PostDataTools.getIstance();
            String postUrl = ip + "/turnoverRecord/findTurnoverRecordsByType";
            String result = post.sendMessageInfo(postUrl, orderCodeMaDto, new String[]{"Content-Type:application/json;charset=utf-8", "jsessionid:" + RequestHolder.getHttpServletRequest().getSession().getId()}, true);
            if (StringUtil.isNotEmpty(result)) {
                JSONObject object = JSON.parseObject(result);
                if (StringUtil.isNotEmpty(object)) {
                    if ("success".equals(object.get("status")) && StringUtil.isNotEmpty(object.get("entity"))) {
                        nos = StringUtil.castList(object.get("entity"), String.class);
                    } else {
                        throw new DataNotFoundException("请检查数据是否正确");
                    }
                }
            }*/
            nos = orderCodeRuleDao.findTurnoverRecordsByTypeInOrder(str1, str2, length);
        } else if ("SLD".equals(code)) {//入库单
            OrderCodeRecodeDto orderCodeRecodeDto = new OrderCodeRecodeDto();
            orderCodeRecodeDto.setCodeNoStart(str1);
            orderCodeRecodeDto.setCodeNoEnd(str2);
            orderCodeRecodeDto.setLength(length);
            orderCodeRecodeDto.setInOutType(1);
            //2020-09-07 18：10 注释 单体架构限制，模块不能拆出，直接通过https请求，并且还是外网，会导致出现问题无法排查，因此暂时不用
           /* PostDataTools post = PostDataTools.getIstance();
            String postUrl = ip + "/record/findRecordsByType";
            String result = post.sendMessageInfo(postUrl, orderCodeRecodeDto, new String[]{"Content-Type:application/json;charset=utf-8", "jsessionid:" + RequestHolder.getHttpServletRequest().getSession().getId()}, true);
            if (StringUtil.isNotEmpty(result)) {
                JSONObject object = JSON.parseObject(result);
                if (StringUtil.isNotEmpty(object)) {
                    if ("success".equals(object.get("status")) && StringUtil.isNotEmpty(object.get("entity"))) {
                        nos = StringUtil.castList(object.get("entity"), String.class);
                    } else {
                        throw new DataNotFoundException("请检查数据是否正确");
                    }
                }
            }*/
            nos = orderCodeRuleDao.findRecordsByTypeInOrder(str1, str2, length, 1);
        } else if ("CKD".equals(code)) {//出库单
            OrderCodeRecodeDto orderCodeRecodeDto = new OrderCodeRecodeDto();
            orderCodeRecodeDto.setCodeNoStart(str1);
            orderCodeRecodeDto.setCodeNoEnd(str2);
            orderCodeRecodeDto.setLength(length);
            orderCodeRecodeDto.setInOutType(2);
            //2020-09-07 18：10 注释 单体架构限制，模块不能拆出，直接通过https请求，并且还是外网，会导致出现问题无法排查，因此暂时不用
           /* PostDataTools post = PostDataTools.getIstance();
            String postUrl = ip + "/record/findRecordsByType";
            String result = post.sendMessageInfo(postUrl, orderCodeRecodeDto, new String[]{"Content-Type:application/json;charset=utf-8", "jsessionid:" + RequestHolder.getHttpServletRequest().getSession().getId()}, true);
            if (StringUtil.isNotEmpty(result)) {
                JSONObject object = JSON.parseObject(result);
                if (StringUtil.isNotEmpty(object)) {
                    if ("success".equals(object.get("status")) && StringUtil.isNotEmpty(object.get("entity"))) {
                        nos = StringUtil.castList(object.get("entity"), String.class);
                    } else {
                        throw new DataNotFoundException("请检查数据是否正确");
                    }
                }
            }*/
             nos = orderCodeRuleDao.findRecordsByTypeInOrder(str1, str2, length, 2);
        } else if ("FFD".equals(code)) {//发货单
            OrderCodeInvoiceDto orderCodeInvoiceDto = new OrderCodeInvoiceDto();
            orderCodeInvoiceDto.setCodeNoStart(str1);
            orderCodeInvoiceDto.setCodeNoEnd(str2);
            orderCodeInvoiceDto.setLength(length);
           /* PostDataTools post = PostDataTools.getIstance();
            String postUrl = ip + "/invoice/findInvoiceNoByType";
            String result = post.sendMessageInfo(postUrl, orderCodeInvoiceDto, new String[]{"Content-Type:application/json;charset=utf-8", "jsessionid:" + RequestHolder.getHttpServletRequest().getSession().getId()}, true);
            if (StringUtil.isNotEmpty(result)) {
                JSONObject object = JSON.parseObject(result);
                if (StringUtil.isNotEmpty(object)) {
                    if ("success".equals(object.get("status")) && StringUtil.isNotEmpty(object.get("entity"))) {
                        nos = StringUtil.castList(object.get("entity"), String.class);
                    } else {
                        throw new DataNotFoundException("请检查数据是否正确");
                    }
                }
            }*/
            nos = orderCodeRuleDao.findInvoiceNoByTypeInOrder(str1, str2, length);
        }
        if (nos != null && nos.size() > 0) {
            for (String no : nos) {
                no = no.substring(str1.length() - 1, no.length());
                //       no = no.substring(0,no.length()-str2.length()-1);
                num = Integer.valueOf(no);
                if (num > temp) {
                    temp = num;
                }
            }
            String pipelinedRule = "%0" + pipelinedNum + "d";
            String number = String.format(pipelinedRule, ++temp);
            orderNo = str1.substring(0, str1.length() - 1) + number + str2.substring(1, str2.length());
            return orderNo;
        }


        return orderNo;
    }

}
