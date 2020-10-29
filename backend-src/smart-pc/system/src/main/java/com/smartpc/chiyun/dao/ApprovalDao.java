
package com.smartpc.chiyun.dao;

import com.smartpc.chiyun.model.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApprovalDao extends JpaRepository<Approval, Long>, JpaSpecificationExecutor<Approval> {


    @Query(value = "SELECT o FROM Approval o where o.associationId = ?1 and o.codeType = ?2")
    List<Approval> findByAssociationIdAndCodeType(Long associationId,String codeType);

}
