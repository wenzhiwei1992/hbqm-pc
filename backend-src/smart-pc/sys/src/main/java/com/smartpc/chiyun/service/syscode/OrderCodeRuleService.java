package com.smartpc.chiyun.service.syscode;

import com.smartpc.chiyun.model.syscode.OrderRule;
import com.smartpc.chiyun.model.syscode.OrderRuleDetail;

import java.util.List;

public interface OrderCodeRuleService {

    List<OrderRule> listAll();

    OrderRule getOrderCodeRuleById(Long id);

    void deleteOrderCodeRuleById(Long id);

    List<OrderRuleDetail>  getDetailByRuleId(Long ruleId);

    void add(OrderRule orderRule);

    void update(OrderRule orderRule);

    OrderRule findByCodeNo(String code);

    String getOrderNoByCode(OrderRule rule);

}
