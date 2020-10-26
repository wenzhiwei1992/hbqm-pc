package com.smartpc.chiyun.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "sys_repeat")
@NoArgsConstructor
public class RepeatEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 操作用户
    private String name;
    // 请求ip
    @Column(name = "request_ip")
    private String requestIp;

    @Column(name = "md_string")
    private String mdString;

    private String token;

    // 方法名
    private String method;

    // 参数
    @Column(columnDefinition = "text")
    private String params;

    // 日志类型
    @Column(name = "log_type")
    private String logType;

    // 创建日期
    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;
}
