package com.smartpc.chiyun.dao.quartz;

import com.smartpc.chiyun.model.quartz.QuartzLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author yue
 * @create 2020/8/13 4:54 下午
 */
public interface QuartzLogDao extends JpaRepository<QuartzLog, Long>, JpaSpecificationExecutor<QuartzLog> {
}
