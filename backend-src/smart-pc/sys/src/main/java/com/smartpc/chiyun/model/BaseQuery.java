package com.smartpc.chiyun.model;

import com.smartpc.chiyun.config.PageInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseQuery<T> {
    private T entity;
    private PageInfo pageInfo;
}
