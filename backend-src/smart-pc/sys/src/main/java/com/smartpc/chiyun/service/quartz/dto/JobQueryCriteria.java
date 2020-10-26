package com.smartpc.chiyun.service.quartz.dto;

import com.smartpc.chiyun.annotations.Query;
import com.smartpc.chiyun.config.PageInfo;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author yue
 * @create 2020/8/13 4:52 下午
 */
@Data
public class JobQueryCriteria {

    private String jobName;

    private Boolean isSuccess;

    private List<Timestamp> createTime;

    private PageInfo pageInfo;

}
