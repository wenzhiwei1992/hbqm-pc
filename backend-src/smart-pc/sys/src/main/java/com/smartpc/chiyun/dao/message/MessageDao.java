package com.smartpc.chiyun.dao.message;


import com.smartpc.chiyun.model.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MessageDao extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {


}
