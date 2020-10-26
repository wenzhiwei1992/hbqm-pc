package com.smartpc.chiyun.config;

import com.smartpc.chiyun.model.sys.ServerConfig;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Properties;

/**
 * 配置类 @Configuration为spring3后提出
 */
@Configuration
public class PropertiesConfig {
    private static final Logger log = LoggerFactory.getLogger(PropertiesConfig.class);
    @Bean
    public ServerConfig serverConfig() {
        ServerConfig sc=new ServerConfig();
        Properties prop=new Properties();
        try {
            String serverConfigPath = System.getProperty("chiyun.server.config");
            InputStream in = new BufferedInputStream(new FileInputStream(serverConfigPath));
            prop.load(in);
            sc.setEnvType(prop.getProperty("env.type"));
            sc.setDatasourceUrl(prop.getProperty("spring.datasource.url"));
            sc.setDatasourceUsername(prop.getProperty("spring.datasource.username"));
            sc.setDatasourcePassword(prop.getProperty("spring.datasource.password"));
            sc.setDatasourceDriver(prop.getProperty("spring.datasource.driver-class-name"));
            sc.setFileSavePath(prop.getProperty("file.save.path"));
            sc.setFileLoadPath(prop.getProperty("file.load.path"));
            sc.setImageMaxSizeKB(prop.getProperty("image.max.size.kb"));
            sc.setAttachMaxSizeKB(prop.getProperty("attach.max.size.kb"));
            sc.setQywxCorpid(prop.getProperty("qywx.corpid"));
            sc.setQywxSecret(prop.getProperty("qywx.secret"));
            sc.setFastUploadPath(prop.getProperty("fastdfs_uploadpath"));
            sc.setTenantId(prop.getProperty("tenantId"));

          /*  sc.setDatasourceType(prop.getProperty("com.alibaba.druid.pool.DruidDataSource"));
            sc.setDruidInitialSize(prop.getProperty("spring.datasource.druid.initial-size"));
            sc.setDruidMinIdle(prop.getProperty("spring.datasource.druid.min-idle"));
            sc.setDruidMaxActive(prop.getProperty("spring.datasource.druid.max-active"));
            sc.setDruidMaxWait(prop.getProperty("spring.datasource.druid.max-wait"));
            sc.setDruidTimeBetweenEvictionRunsMillis(prop.getProperty("spring.datasource.druid.time-between-eviction-runs-millis"));
            sc.setDruidMinEvictableIdleTimeMillis(prop.getProperty("spring.datasource.druid.min-evictable-idle-time-millis"));
            sc.setDruidMaxEvictableIdleTimeMillis(prop.getProperty("spring.datasource.druid.max-evictable-idle-time-millis"));
            sc.setDruidTestWhileIdle(prop.getProperty("spring.datasource.druid.test-while-idle"));
            sc.setDruidTestOnBorrow(prop.getProperty("spring.datasource.druid.test-on-borrow"));
            sc.setDruidTestOnReturn(prop.getProperty("spring.datasource.druid.test-on-return"));
            sc.setDruidPoolPreparedStatements(prop.getProperty("spring.datasource.druid.pool-prepared-statements"));
            sc.setDruidFilters(prop.getProperty("spring.datasource.druid.filters"));
            sc.setDruidConnectionPropertiesMergeSql(prop.getProperty("spring.datasource.druid.connection-properties=druid.stat.mergeSql"));
            sc.setDruidValidationQuery(prop.getProperty("spring.datasource.druid.validation-query"));*/

            /*sc.setDruidWebStatFilterEnabled(prop.getProperty("spring.datasource.druid.webStatFilter.enabled"));
            sc.setDruidStatViewServletEnabled(prop.getProperty("spring.datasource.druid.stat-view-servlet.enabled"));
            sc.setDruidUrlPattern(prop.getProperty("spring.datasource.druid.url-pattern"));
            sc.setDruidResetEnable(prop.getProperty("spring.datasource.druid.reset-enable"));
            sc.setDruidFilterStatEnabled(prop.getProperty("spring.datasource.druid.filter.stat.enabled"));
            sc.setDruidFiltertatLogSlowSql(prop.getProperty("spring.datasource.druid.filter.stat.log-slow-sql"));
            sc.setDruidFilterStatSlowSqlMillis(prop.getProperty("spring.datasource.druid.filter.stat.slow-sql-millis"));
            sc.setDruidFilterStatMergeSql(prop.getProperty("spring.datasource.druid.filter.stat.merge-sql"));
            sc.setDruidFilterWallConfigMultiStatementAllow(prop.getProperty("spring.datasource.druid.filter.wall.config.multi-statement-allow"));
            sc.setDruidWebStatFilterEnableded(prop.getProperty("spring.datasource.druid.web-stat-filter.enabled"));*/

            try{
                JSONParser parser = new JSONParser();
                File file = new ClassPathResource("org-db.json").getFile();
                JSONArray arr= (JSONArray)parser.parse(new FileReader(file));
                sc.setOrgs(arr);
            }catch(Exception ex){

            }


        } catch (IOException e) {
            log.error("server-config.properties配置文件读取出错", e);
        }
        return sc;

    }
}
