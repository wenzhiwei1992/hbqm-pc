package com.smartpc.chiyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @Author yue
 * @create 2020/7/22 10:56 上午
 */
@SpringBootApplication
@EnableCaching
public class ReportApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        // new SpringApplicationBuilder(MultitenancyApplication.class).run(args);
        SpringApplication.run(ReportApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReportApplication.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }
}
