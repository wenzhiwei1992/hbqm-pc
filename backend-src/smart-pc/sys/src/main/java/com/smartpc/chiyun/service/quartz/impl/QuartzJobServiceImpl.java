package com.smartpc.chiyun.service.quartz.impl;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.config.quartz.QuartzManager;
import com.smartpc.chiyun.dao.quartz.QuartzJobDao;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.quartz.QuartzJob;
import com.smartpc.chiyun.model.quartz.QuartzLog;
import com.smartpc.chiyun.service.quartz.QuartzJobService;
import com.smartpc.chiyun.service.quartz.dto.JobQueryCriteria;
import com.smartpc.chiyun.utils.StringUtils;
import com.smartpc.chiyun.utils.ValidationUtil;
import com.smartpc.chiyun.vo.ManualPageVo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * @Author yue
 * @create 2020/8/13 4:10 下午
 */
@Slf4j
@Service
public class QuartzJobServiceImpl implements QuartzJobService {
    @Autowired
    QuartzJobDao quartzJobDao;
    @Autowired
    QuartzManager quartzManager;

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public ManualPageVo<QuartzJob> queryAllLog(JobQueryCriteria criteria) {
        Integer page = criteria.getPageInfo().getCurrentPage() - 1;
        Integer pageSize = criteria.getPageInfo().getPageSize();
        List<QuartzJob> quartzJobList = new ArrayList<>();
        String sql = " SELECT  job_name,cron,bean_name,method_name,is_pause,remarks,id,pause_after_failure,params FROM sys_quartz_job WHERE 1=1 ";
        String sqlCount = " SELECT count(1) FROM sys_quartz_job WHERE 1=1 ";
        Query nativeQueryCount = entityManager.createNativeQuery(sqlCount);
        int size = ((Number) nativeQueryCount.getSingleResult()).intValue();

        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setFirstResult(page * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Object[]> objectList = nativeQuery.getResultList();
        QuartzJob quartzJob = null;
        for (int i = 0; i < objectList.size(); i++) {
            Object[] objects = objectList.get(i);
            quartzJob = new QuartzJob();
            String jobName = String.valueOf(objects[0] == null ? "" : objects[0]);
            String cron = String.valueOf(objects[1] == null ? "" : objects[1]);
            String beanName = String.valueOf(objects[2] == null ? "" : objects[2]);
            String methodName = String.valueOf(objects[3] == null ? "" : objects[3]);
            String isPause = String.valueOf(objects[4] == null ? "0" : objects[4]);
            String remarks = String.valueOf(objects[5] == null ? "" : objects[5]);
            String id = String.valueOf(objects[6] == null ? "0L" : objects[6]);
            String pauseAfterFailure = String.valueOf(objects[7] == null ? "0" : objects[7]);
            String params = String.valueOf(objects[8] == null ? "" : objects[8]);
            quartzJob.setJobName(jobName);
            quartzJob.setCron(cron);
            quartzJob.setBeanName(beanName);
            quartzJob.setMethodName(methodName);
            quartzJob.setIsPause(Boolean.valueOf(isPause));
            quartzJob.setRemarks(remarks);
            quartzJob.setId(Long.parseLong(id));
            quartzJob.setPauseAfterFailure(Boolean.valueOf(pauseAfterFailure));
            quartzJob.setParams(params);
            quartzJobList.add(quartzJob);
        }
        ManualPageVo<QuartzJob> manualPageVo = new ManualPageVo<>();
        manualPageVo.setList(quartzJobList);
        manualPageVo.setTotalCount(size);
        return manualPageVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addQuartzJob(QuartzJob resources) {
        ValidationUtil.isNull(resources.getCron(), "cron表达式格式", "cron", null);
        ValidationUtil.isNull(resources.getJobName(), "任务名称", "jobName", null);
        ValidationUtil.isNull(resources.getBeanName(), "Bean类", "beanName", null);
        ValidationUtil.isNull(resources.getMethodName(), "方法", "methodName", null);
        if (!CronExpression.isValidExpression(resources.getCron())) {
            throw new DataNotFoundException("cron表达式格式错误");
        }

        resources = quartzJobDao.save(resources);
        quartzManager.addJob(resources);
    }

    @Override
    public void updateQuartzJob(QuartzJob resources) {
        ValidationUtil.isNull(resources.getCron(), "cron表达式格式", "cron", null);
        ValidationUtil.isNull(resources.getJobName(), "任务名称", "jobName", null);
        ValidationUtil.isNull(resources.getBeanName(), "Bean类", "beanName", null);
        ValidationUtil.isNull(resources.getMethodName(), "方法", "methodName", null);
        if (!CronExpression.isValidExpression(resources.getCron())) {
            throw new DataNotFoundException("cron表达式格式错误");
        }
        resources = quartzJobDao.save(resources);
        quartzManager.updateJobCron(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQuartzJob(String ids) {
        String[] split = ids.split(";");
        Set<Long> list = new HashSet<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        List<QuartzJob> quartzJobList = new ArrayList<>();
        for (Long id : list) {
            QuartzJob quartzJob = findById(id);
            quartzManager.deleteJob(quartzJob);
            quartzJobList.add(quartzJob);
        }
        quartzJobDao.deleteAll(quartzJobList);
    }

    @Override
    public QuartzJob findById(Long id) {
        return quartzJobDao.findById(id).get();
    }

    @Override
    public List<QuartzJob> findByIsPauseIsTrue() {
        return quartzJobDao.findByIsPauseIsTrue();
    }

    @Override
    public void updateIsPause(Long id) {
        QuartzJob quartzJob = quartzJobDao.findById(id).get();
        if (quartzJob.getIsPause()) {
            quartzManager.pauseJob(quartzJob);
            quartzJob.setIsPause(false);
        } else {
            quartzManager.resumeJob(quartzJob);
            quartzJob.setIsPause(true);
        }
        quartzJobDao.save(quartzJob);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuartzJobTime(QuartzJob resources) {
        ValidationUtil.isNull(resources.getCron(), "cron表达式格式", "cron", null);
        ValidationUtil.isNull(resources.getJobName(), "任务名称", "jobName", null);
        if (!CronExpression.isValidExpression(resources.getCron())) {
            throw new DataNotFoundException("cron表达式格式错误");
        }
        QuartzJob quartzJob = quartzJobDao.findByJobName(resources.getJobName(), AppUtil.getCurrentUserOrgId());
        quartzJob.setCron(resources.getCron());
        resources = quartzJobDao.save(quartzJob);
        quartzManager.updateJobCron(resources);
    }

}
