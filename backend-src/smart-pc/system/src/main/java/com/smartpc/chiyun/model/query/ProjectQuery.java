package com.smartpc.chiyun.model.query;

import com.smartpc.chiyun.config.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectQuery {
    private PageInfo pageInfo;
    private String projectName;
    private String projectNo;
    private String customer;
    private String workPlace;
    private String approvalStatus;
    private Long provinceId;
    private Long cityId;
    private List<Long> orgIds;
}
