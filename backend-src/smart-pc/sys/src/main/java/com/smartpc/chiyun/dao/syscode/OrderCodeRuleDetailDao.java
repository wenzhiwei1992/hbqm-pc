package com.smartpc.chiyun.dao.syscode;

import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import com.smartpc.chiyun.model.syscode.OrderRuleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderCodeRuleDetailDao extends JpaRepository<OrderRuleDetail,Long>,JpaSpecificationExecutor<CodeExplainContent> {

    List<OrderRuleDetail> findAllByRuleId(Long ruleId);
}
