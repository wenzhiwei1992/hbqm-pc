package com.smartpc.chiyun.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 定时任务测试类
 *
 * @Author yue
 * @create 2020/8/14 5:22 下午
 */
@Slf4j
@Component
public class TestTask {

    public void run() {
        log.info("----------run 执行成功------------");
    }

    public void runParams(String str) {
        log.info("----------runParams 执行成功，参数为： {}--------" + str);
    }
}
