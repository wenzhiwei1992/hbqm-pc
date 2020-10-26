package com.smartpc.chiyun.config.tenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author yue
 * @create 2020/2/13 10:42 上午
 */
@Data
@Component
@ConfigurationProperties(prefix = "tenant")
@PropertySource(value = {"classpath:/application.properties"},
        ignoreResourceNotFound = false, encoding = "UTF-8", name = "application.properties")
public class TenantProperties {
    /**
     * 需要进行租户解析的租户表
     */
    private List<String> tables;
}
