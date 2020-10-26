package com.smartpc.chiyun.dao.quartz;


import com.smartpc.chiyun.model.quartz.QuartzJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author yue
 * @create 2020/8/13 4:07 下午
 */
@Repository
public interface QuartzJobDao extends JpaRepository<QuartzJob, Long> {
    /**
     * 查询启用的任务
     *
     * @return List
     */
    @Query(value = "select * from sys_quartz_job where is_pause =true ", nativeQuery = true)
    List<QuartzJob> findByIsPauseIsTrue();


    @Query(value = "select * from sys_quartz_job where job_name = ?1 and org_id = ?2 ", nativeQuery = true)
    QuartzJob findByJobName(String jobName,Long orgId);
}
