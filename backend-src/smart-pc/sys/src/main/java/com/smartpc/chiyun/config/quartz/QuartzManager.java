package com.smartpc.chiyun.config.quartz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.constant.QuartzConstant;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.quartz.QuartzJob;
import com.smartpc.chiyun.utils.SpringContextUtil;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 任务管理
 *
 * @Author yue
 * @create 2020/8/13 3:43 下午
 */

@Slf4j
@Component
@Scope("singleton")
public class QuartzManager {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    private Scheduler scheduler;

    @Autowired
    AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory;

    private static final String JOB_NAME = "TASK_";

    //初始化注解
    @PostConstruct
    public void init() {
        try {
            log.info("----QuartzManager----init--------");
            this.scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(autowiringSpringBeanJobFactory);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加任务
     *
     * @param quartzJob
     */
    public void addJob(QuartzJob quartzJob) {
        try {

            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(AbstractTask.class)
                    .withIdentity(JOB_NAME + quartzJob.getId())//唯一标识
                    .build();

            //通过触发器名和cron 表达式创建 Trigger
            Trigger cronTrigger = newTrigger()
                    .withIdentity(JOB_NAME + quartzJob.getId())
                    .startNow()//立即生效
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCron()))//每隔时长执行
                    .build();//一直执行

            cronTrigger.getJobDataMap().put(QuartzJob.JOB_KEY, quartzJob);

            //重置启动时间
            ((CronTriggerImpl) cronTrigger).setStartTime(new Date());

            //执行定时任务
            scheduler.scheduleJob(jobDetail, cronTrigger);
            scheduler.start();
            // 暂停任务
            if (!quartzJob.getIsPause()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            log.error("创建定时任务失败", e);
            throw new DataNotFoundException("创建定时任务失败");
        }
    }


    /**
     * 更新job cron表达式
     *
     * @param quartzJob /
     */
    public void updateJobCron(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCron());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置启动时间
            ((CronTriggerImpl) trigger).setStartTime(new Date());
            trigger.getJobDataMap().put(QuartzJob.JOB_KEY, quartzJob);

            scheduler.rescheduleJob(triggerKey, trigger);
            // 暂停任务
            if (!quartzJob.getIsPause()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            log.error("更新定时任务失败", e);
            throw new DataNotFoundException("更新定时任务失败");
        }

    }

    /**
     * 删除一个job
     *
     * @param quartzJob /
     */
    public void deleteJob(QuartzJob quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            log.error("删除定时任务失败", e);
            throw new DataNotFoundException("删除定时任务失败");
        }
    }

    /**
     * 添加任务
     *
     * @param jobName  任务名称
     * @param jobClass
     * @param cronExp
     * @param data
     * @return
     */
    public boolean addJob(String jobName, Class jobClass, String cronExp, Object data) throws Exception {
        boolean result = false;
        if (!CronExpression.isValidExpression(cronExp)) {
            log.error(String.format("----------Corn表达式格式不正确---------", cronExp));
            return result;
        }
        //如果jobName已存在
        if (checkJob(jobName)) {
            log.error("---------------jobName已存在----------");
            return result;
        }

        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp)
                .withMisfireHandlingInstructionDoNothing();
        // 构建job信息
        //JobDataMap底层实现了JDK的Map接口，可以通过key,value形式存储数据
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("data", data);
        JobDetail jobDetail = JobBuilder.newJob((Class<Job>) jobClass)
                //.withIdentity(new JobKey(jobName, QuartzConstant.JOB_GROUP_NAME))
                .withIdentity(JOB_NAME + 1)
                .setJobData(jobDataMap)
                .build();
        //按新的cron表达式构建一个新的trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(JOB_NAME + 1)
                //.withIdentity(new TriggerKey(jobName, QuartzConstant.JOB_TRIGGER_GROUP_NAME))
                .withSchedule(scheduleBuilder)
                .build();
        //重置启动时间
        // ((CronTriggerImpl) trigger).setStartTime(new Date());
        //执行定时任务
        scheduler.scheduleJob(jobDetail, trigger);

        return result;
    }

    /**
     * 修改任务
     *
     * @param jobName
     * @param cronExp
     * @return
     */
    public boolean updateJob(String jobName, String cronExp) {
        boolean result = false;
        if (!CronExpression.isValidExpression(cronExp)) {

            log.error(String.format("Illegal cron expression format(%s)", cronExp));
            return result;
        }
        JobKey jobKey = new JobKey(jobName, QuartzConstant.JOB_GROUP_NAME);
        TriggerKey triggerKey = new TriggerKey(jobName, QuartzConstant.JOB_TRIGGER_GROUP_NAME);
        try {
            if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                Trigger newTrigger = TriggerBuilder.newTrigger()
                        .forJob(jobDetail)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                        .withIdentity(new TriggerKey(jobName, QuartzConstant.JOB_TRIGGER_GROUP_NAME))
                        .build();
                scheduler.rescheduleJob(triggerKey, newTrigger);
                result = true;
            } else {

                log.error(String.format("update job name:%s,group name:%s or trigger name:%s,group name:%s not exists.."
                        , jobKey.getName(), jobKey.getGroup(), triggerKey.getName(), triggerKey.getGroup()));
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            log.error(String.format("update job name:%s,group name:%s failed!"
                    , jobKey.getName(), jobKey.getGroup()));
        }
        return result;
    }

    /**
     * 检查是否存在
     *
     * @param jobName
     * @return
     */
    public Boolean checkJob(String jobName) {
        JobKey jobKey = new JobKey(jobName, QuartzConstant.JOB_GROUP_NAME);
        try {
            //如果已经存在当前jobKey
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除任务
     *
     * @param jobName
     * @return
     */
    public boolean deleteJob(String jobName) {
        boolean result = false;
        JobKey jobKey = new JobKey(jobName, QuartzConstant.JOB_GROUP_NAME);
        try {
            if (scheduler.checkExists(jobKey)) {
                result = scheduler.deleteJob(jobKey);
            } else {

                log.error(String.format("delete job name:%s,group name:%s not exists."
                        , jobKey.getName(), jobKey.getGroup()));
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);

            log.error(String.format("delete job name:%s,group name:%s failed!"
                    , jobKey.getName(), jobKey.getGroup()));
        }
        return result;
    }

    /**
     * 获取一个任务的信息
     *
     * @param jobName
     * @return
     */
    public JSONObject getOneJob(String jobName) {
        JobKey jobKey = new JobKey(jobName, QuartzConstant.JOB_GROUP_NAME);
        try {
            JobDetail jobDetail = schedulerFactory.getScheduler().getJobDetail(jobKey);
            List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
            Trigger trigger = triggers.get(0);
            TriggerKey triggerKey = trigger.getKey();
            CronTrigger cornTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //corn表达式
            String corn = cornTrigger.getCronExpression();
            //下次运行时间
            Date nextFireTime = trigger.getNextFireTime();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            String data = jobDetail.getJobDataMap().getString("data");
            JSONObject object = new JSONObject();
            object.put("jobName", jobName);
            object.put("corn", corn);
            object.put("jobGroup", jobKey.getGroup());
            object.put("nextFireTime", nextFireTime);
            object.put("triggerState", triggerState);
            object.put("className", JSONObject.parseObject(data).get("className"));
            object.put("methodName", JSONObject.parseObject(data).get("methodName"));
            return object;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getAllJobs() {
        JSONArray jsonArray = new JSONArray();

        // String data = "";
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();

            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    Trigger trigger = triggers.get(0);
                    TriggerKey triggerKey = trigger.getKey();
                    String data = scheduler.getJobDetail(jobKey).getJobDataMap().getString("data");
                    CronTrigger cornTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    //corn表达式
                    String corn = cornTrigger.getCronExpression();
                    //下次运行时间
                    Date nextFireTime = trigger.getNextFireTime();
                    /**
                     * TriggerState
                     *          NONE,
                     *         NORMAL,
                     *         PAUSED,
                     *         COMPLETE,
                     *         ERROR,
                     *         BLOCKED;
                     */
                    //状态
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
                    JSONObject object = new JSONObject();
                    object.put("jobName", jobName);
                    object.put("corn", corn);
                    object.put("jobGroup", jobGroup);
                    object.put("nextFireTime", nextFireTime);
                    object.put("triggerState", triggerState);
                    object.put("className", JSONObject.parseObject(data).get("className"));
                    object.put("methodName", JSONObject.parseObject(data).get("methodName"));
                    jsonArray.add(object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 手动执行一个任务（通过jobName）
     *
     * @param jobName
     */
    public void runOneJob(String jobName) {

        try {
            JobKey jobKey = new JobKey(jobName, QuartzConstant.JOB_GROUP_NAME);
            JobDetail jobDetail = schedulerFactory.getScheduler().getJobDetail(jobKey);
            String data = jobDetail.getJobDataMap().getString("data");
            String className = JSONObject.parseObject(data).getString("className");
            String methodName = JSONObject.parseObject(data).getString("methodName");
            Object bean = SpringContextUtil.getBean(Class.forName(className));
            bean.getClass().getMethod(methodName).invoke(bean);
            //return ResponseBuilder.buildResponse();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException("运行出现异常::" + ExceptionUtils.getStackTrace(e));
            // return ResponseBuilder.buildResponse("4001", "运行出现异常::" + ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 手动运行所有任务
     *
     * @return
     */
    public void runAllJob() {
        List<Object> errorMessage = new ArrayList<>();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String className = "";
                    String methodName = "";
                    try {
                        String data = scheduler.getJobDetail(jobKey).getJobDataMap().getString("data");
                        className = JSONObject.parseObject(data).getString("className");
                        methodName = JSONObject.parseObject(data).getString("methodName");
                        Object bean = SpringContextUtil.getBean(Class.forName(className));
                        bean.getClass().getMethod(methodName).invoke(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new DataNotFoundException(className + methodName + "运行失败" + ExceptionUtils.getStackTrace(e));
                        //errorMessage.add(className + methodName + "运行失败" + ExceptionUtils.getStackTrace(e));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      /*  if (errorMessage.size() > 0) {
            throw new DataNotFoundException(errorMessage);
        }*/

    }

    /**
     * 暂停一个job
     *
     * @param quartzJob /
     */
    public void pauseJob(QuartzJob quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            log.error("定时任务暂停失败", e);
            throw new DataNotFoundException("定时任务暂停失败");
        }
    }

    /**
     * 恢复一个job
     *
     * @param quartzJob /
     */
    public void resumeJob(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob);
            }
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            log.error("恢复定时任务失败", e);
            throw new DataNotFoundException("恢复定时任务失败");
        }
    }


}
