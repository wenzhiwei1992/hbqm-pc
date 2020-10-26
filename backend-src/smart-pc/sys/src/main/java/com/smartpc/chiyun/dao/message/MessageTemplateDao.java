package com.smartpc.chiyun.dao.message;


import com.smartpc.chiyun.model.message.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageTemplateDao extends JpaRepository<MessageTemplate, Integer>, JpaSpecificationExecutor<MessageTemplate> {

    @Query(value = "SELECT  *  from message_template where type_code=?1 and org_id = ?2  ORDER BY id   desc LIMIT 1 ",nativeQuery = true)
    MessageTemplate findByTypeAndOrgId(String typeCode,Long orgId);

    @Query(value = "SELECT  *  from message_template where type_code=?1 and status=1  ORDER BY id   desc LIMIT 1 ",nativeQuery = true)
    List<MessageTemplate>  findMessageTemplateByTypeCode(String type);

    List<MessageTemplate> findAllByOrgId(Long orgId);

    MessageTemplate  findMessageTemplateByTypeCodeAndOrgId(String type,Long orgId);

}
