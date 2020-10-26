package com.smartpc.chiyun.dao.syscode;

import com.smartpc.chiyun.model.syscode.CodeExplain;
import com.smartpc.chiyun.model.syscode.OrderRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zihao
 */
@Repository
public interface OrderCodeRuleDao extends JpaRepository<OrderRule, Long>, JpaSpecificationExecutor<CodeExplain> {

    @Query("select e from OrderRule e where e.id = ?1")
    OrderRule getOrderCodeRuleById(Long id);

    List<OrderRule> findByCodeNoAndEndTimeIsNotNull(String codeNo);

    OrderRule findByCodeNoAndEndTimeIsNull(String codeNo);

    List<OrderRule> findByEndTimeIsNull();

    /**
     * 查询单号（用于单号生成）
     *
     * @return
     */
    @Query(value = "select turnover_record_no from  ma_turnover_record" +
            " where turnover_record_no like ?1 and turnover_record_no like ?2 and LENGTH(turnover_record_no)= ?3 ", nativeQuery = true)
    List<String> findTurnoverRecordsByTypeInOrder(String code1, String code2, int length);

    /**
     * 查询单号（用于单号生成）
     *
     * @return
     */
    @Query(value = "select in_bound_order from  yd_record" +
            " where in_bound_order like ?1 and in_bound_order like ?2 and LENGTH(in_bound_order)= ?3 and in_out_type=?4 ", nativeQuery = true)
    List<String> findRecordsByTypeInOrder(String code1, String code2, int length, int in_out_type);

    /**
     * 查询单号（用于单号生成）
     *
     * @return
     */
    @Query(value = "select deliver_goods_no from  yd_record" +
            " where deliver_goods_no like ?1 and deliver_goods_no like ?2 and LENGTH(deliver_goods_no)= ?3  ", nativeQuery = true)
    List<String> findInvoiceNoByTypeInOrder(String code1, String code2, int length);
}
