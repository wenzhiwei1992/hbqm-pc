package com.smartpc.chiyun.service.sys.dto;

import com.smartpc.chiyun.annotations.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author yue
 * @create 2020/3/12 5:20 下午
 */
@Data
public class MenuQueryCriteria {
    @Query(blurry = "name,path,component")
    private String blurry;
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

}
