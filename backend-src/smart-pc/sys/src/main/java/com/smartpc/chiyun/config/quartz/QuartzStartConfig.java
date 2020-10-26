package com.smartpc.chiyun.config.quartz;

import com.alibaba.fastjson.JSON;
import com.smartpc.chiyun.model.quartz.QuartzJob;
import com.smartpc.chiyun.service.quartz.QuartzJobService;
import lombok.extern.apachecommons.CommonsLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目启动时重新激活启用的定时任务
 *
 * @Author yue
 * @create 2020/8/13 4:14 下午
 */
@Component
public class QuartzStartConfig implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(QuartzStartConfig.class);
    @Autowired
    QuartzManager quartzManager;

    @Autowired
    QuartzJobService quartzJobService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        /**
         * 默认的任务执行类 com.dh.quartz.AbstractTask
         * 可通过继承 实现多个,
         * 此处只有一个类，通过反向代理和spring容器，调用指定方法
         */
        /**
         * （如果不想创建多个job,也可关闭此注解，然后在运行的方法上加锁等方式来阻止并发）
         * @DisallowConcurrentExecution
         * 此标记用在实现Job的类上面, 意思是不允许并发执行,
         * 按照我之前的理解是 不允许调度框架在同一时刻调用Job类，后来经过测试发现并不是这样，
         * 而是Job(任务)的执行时间[比如需要10秒]大于任务的时间间隔[Interval（5秒)],
         * 那么默认情况下,调度框架为了能让 任务按照我们预定的时间间隔执行,会马上启用新的线程执行任务。
         * 否则的话会等待任务执行完毕以后 再重新执行！（这样会导致任务的执行不是按照我们预先定义的时间间隔执行）
         * 测试代码，这是官方提供的例子。设定的时间间隔为3秒,但job执行时间是5秒,
         * 设置@DisallowConcurrentExecution以后程序会等任务执行完毕以后再去执行,否则会在3秒时再启用新的线程执行
         *
         *
         *
         * JobDetail定义的是任务数据，而真正的执行逻辑是在Job中。
         * 这是因为任务是有可能并发执行，如果Scheduler直接使用Job，就会存在对同一个Job实例并发访问的问题。而JobDetail & Job 方式，Sheduler每次执行，都会根据JobDetail创建一个新的Job实例，这样就可以规避并发访问的问题。
         */
        //初始化所有任务

        log.info("--------------------注入定时任务---------------------");
        List<QuartzJob> quartzJobs = quartzJobService.findByIsPauseIsTrue();
        for (QuartzJob quartzJob : quartzJobs) {
            quartzManager.addJob(quartzJob);
            //quartzManager.addJob(quartzJob.getJobName(), AbstractTask.class, quartzJob.getCron(), JSON.toJSONString(quartzJob));
        }
        log.info("--------------------定时任务注入完成---------------------");

    }
}
