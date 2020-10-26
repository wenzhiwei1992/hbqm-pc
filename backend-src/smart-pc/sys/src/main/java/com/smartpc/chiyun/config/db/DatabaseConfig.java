package com.smartpc.chiyun.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.smartpc.chiyun.config.web.OrgIntercepor;
import com.smartpc.chiyun.model.sys.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaAuditing
@Slf4j
public class DatabaseConfig {

    @Autowired
    private JpaProperties jpaProperties;
    @Autowired
    private ServerConfig serverConfig;


    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }


   /* @Bean
    public DataSource druidDataSource() {
        DruidDataSource ds = new DruidDataSource();
        ds.setUsername(serverConfig.getDatasourceUsername());
        ds.setPassword(serverConfig.getDatasourcePassword());
        ds.setUrl(serverConfig.getDatasourceUrl());
        ds.setDriverClassName(serverConfig.getDatasourceDriver());
        ds.setDbType(serverConfig.getDatasourceType());
        ds.setInitialSize(Integer.valueOf(serverConfig.getDruidInitialSize()));
        ds.setMinIdle(Integer.valueOf(serverConfig.getDruidMinIdle()));
        ds.setMaxActive(Integer.valueOf(serverConfig.getDruidMaxActive()));
        ds.setMaxWait(Integer.valueOf(serverConfig.getDruidMaxWait()));
        ds.setTimeBetweenEvictionRunsMillis(Integer.valueOf(serverConfig.getDruidTimeBetweenEvictionRunsMillis()));
        ds.setMinEvictableIdleTimeMillis(Integer.valueOf(serverConfig.getDruidMinEvictableIdleTimeMillis()));
        ds.setMaxEvictableIdleTimeMillis(Integer.valueOf(serverConfig.getDruidMaxEvictableIdleTimeMillis()));
        ds.setTestWhileIdle(Boolean.valueOf(serverConfig.getDruidTestWhileIdle()));
        ds.setTestOnBorrow(Boolean.valueOf(serverConfig.getDruidTestOnBorrow()));
        ds.setTestOnReturn(Boolean.valueOf(serverConfig.getDruidTestOnReturn()));
        ds.setPoolPreparedStatements(Boolean.valueOf(serverConfig.getDruidPoolPreparedStatements()));
        try {
            ds.setFilters(serverConfig.getDruidFilters());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ds.setConnectionProperties(serverConfig.getDruidConnectionPropertiesMergeSql());
        ds.setValidationQuery(serverConfig.getDruidValidationQuery());
        //ds.setWebStatFilter();
        //ds.setResetEnable();

        return ds;
    }*/

    @Bean
    @Primary
    public DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .url(serverConfig.getDatasourceUrl())
                .username(serverConfig.getDatasourceUsername())
                .password(serverConfig.getDatasourcePassword())
                .driverClassName(serverConfig.getDatasourceDriver())
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
                                                                       CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
        String envType = serverConfig.getEnvType();
        if (!StringUtils.isEmpty(envType) && envType.equals(ServerConfig.ENV_TYPE_DEV)) {
            properties.put(Environment.SHOW_SQL, "true");
            properties.put(Environment.FORMAT_SQL, "true");
        }
        //JPA拦截配置
        properties.put(Environment.STATEMENT_INSPECTOR, OrgIntercepor.class);
        //自动更新表结构
        properties.put(Environment.HBM2DDL_AUTO, "update");
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.smartpc.chiyun");
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaPropertyMap(properties);
        return em;
    }
}
