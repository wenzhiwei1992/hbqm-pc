package com.smartpc.chiyun.config.quartz;


import com.smartpc.chiyun.model.quartz.QuartzJob;


import com.smartpc.chiyun.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;


/**
 * 任务执行
 *
 * @Author yue
 * @create 2020/8/13 3:36 下午
 */
@Slf4j
@DisallowConcurrentExecution
public class AbstractTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        QuartzJob quartzJob = (QuartzJob) jobExecutionContext.getMergedJobDataMap().get(QuartzJob.JOB_KEY);
        try {
            Object bean = SpringContextUtil.getBean(quartzJob.getBeanName());
            //带参数
            if (StringUtils.isNotBlank(quartzJob.getParams())) {
                bean.getClass().getMethod(quartzJob.getMethodName(), String.class).invoke(bean, quartzJob.getParams());
            } else {
                bean.getClass().getMethod(quartzJob.getMethodName()).invoke(bean);
            }
            //QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(),
            //quartzJob.getParams());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("job execute failed!");
        }
    }
}
