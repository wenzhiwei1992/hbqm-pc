package com.smartpc.chiyun.model.sys;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;

@Getter
@Setter
public class ServerConfig {
    public static final String ENV_TYPE_PROD = "prod";
    public static final String ENV_TYPE_DEV = "dev";
    public static final String ENV_TYPE_TEST = "test";
    private String envType;//为dev的时候会输出sql日志
    private String datasourceUrl;
    private String datasourceUsername;
    private String datasourcePassword;
    private String datasourceDriver;
    private String fileSavePath;
    private String fileLoadPath;
    private String imageMaxSizeKB;
    private String attachMaxSizeKB;
    private String qywxCorpid;
    private String qywxSecret;
    private String tenantId;
    private String datasourceType;
    /*初始连接数*/
    private String druidInitialSize;
    /*最小连接数*/
    private String druidMinIdle;
    /*最大连接数*/
    private String druidMaxActive;
    /*获取连接超时时间*/
    private String druidMaxWait;
    /*配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒*/
    private String druidTimeBetweenEvictionRunsMillis;
    /*连接在池中最小生存的时间*/
    private String druidMinEvictableIdleTimeMillis;
    /*连接在池中最大生存的时间*/
    private String druidMaxEvictableIdleTimeMillis;
    private String druidTestWhileIdle;
    private String druidTestOnBorrow;
    private String druidTestOnReturn;
    /*是否缓存preparedStatement，也就是PSCache  官方建议MySQL下建议关闭*/
    private String druidPoolPreparedStatements;
    /*配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙*/
    private String druidFilters;
    /*通过connectProperties属性来打开mergeSql功能；慢SQL记录*/
    private String druidConnectionPropertiesMergeSql;
    /*检测连接是否有效*/
    private String druidValidationQuery;
   /* *//*配置监控统计*//*
    private String druidWebStatFilterEnabled;
    private String druidStatViewServletEnabled;
    private String druidUrlPattern;
    private String druidResetEnable;
    private String druidFilterStatEnabled;
    *//*记录慢SQL*//*
    private String druidFiltertatLogSlowSql;
    private String druidFilterStatSlowSqlMillis;
    private String druidFilterStatMergeSql;
    private String druidFilterWallConfigMultiStatementAllow;
    *//*配置DruidStatFilter*//*
    private String druidWebStatFilterEnableded;*/

    private String fastUploadPath;

    private JSONArray orgs;
}
