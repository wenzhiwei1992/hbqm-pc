package com.smartpc.chiyun.vo;

/**
 * @Author yue
 * @create 2020/6/11 12:22 上午
 */

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ManualPageVo<T> {

    private Integer totalCount;
    private HashMap<String, T> lists;
    private List<T> list;
}
