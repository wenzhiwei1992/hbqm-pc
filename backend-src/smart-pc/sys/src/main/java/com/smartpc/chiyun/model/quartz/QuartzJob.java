package com.smartpc.chiyun.model.quartz;

import com.smartpc.chiyun.model.CommonProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @author: yue
 * * @date: 2020/8/13 13:18
 */
@Entity
@ApiModel(value = "任务调度")
@Data
@Table(name = "sys_quartz_job")
public class QuartzJob extends CommonProperties {
    private static final long serialVersionUID = 1L;
    public static final String JOB_KEY = "JOB_KEY";
    /**
     * 任务名称
     */
    @ApiModelProperty(value = "定时器名称")
    @Column(name = "job_name")
    private String jobName;

    /**
     * 表达式
     */
    @ApiModelProperty(value = "表达式")
    @Column(name = "cron")
    private String cron;

    /**
     * 类
     */
    @ApiModelProperty(value = "类")
    @Column(name = "bean_name")
    private String beanName;

    /**
     * 方法
     */
    @ApiModelProperty(value = "方法")
    @Column(name = "method_name")
    private String methodName;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    @Column(name = "is_pause")
    private Boolean isPause = false;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String remarks;

    @ApiModelProperty(value = "失败后暂停")
    @Column(name = "pause_after_failure")
    private Boolean pauseAfterFailure;

    @ApiModelProperty(value = "参数")
    private String params;


}
