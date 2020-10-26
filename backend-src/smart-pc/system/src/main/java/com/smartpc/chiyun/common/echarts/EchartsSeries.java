package com.smartpc.chiyun.common.echarts;

import lombok.Data;

import java.util.List;

@Data
public class EchartsSeries {

    private String name;
    /**
     * 默认 bar
     */
    private String type;
    /**
     * 默认 project
     */
    private String stack;
    /**
     * 根据项目区分
     */
    private List data;
}
