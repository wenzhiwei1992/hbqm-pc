package com.smartpc.chiyun.config.swapper;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author yue
 */
// 启动时加载类
@Configuration
// 启用Swagger API文档
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class Swagger2Config {

    private ApiInfo apiInfo(String groupName) {
        return new ApiInfoBuilder().title("持云工程MES系统API-" + groupName)
                .description("持云工程MES系统API-" + groupName)
                .termsOfServiceUrl("http://chiyun.valuprosys.com:8082/").version("1.0")
                .contact(new Contact("持云工程", "+86 021-66035008", ""))
                .licenseUrl("http://chiyun.valuprosys.com:8082/").build();
    }

    @Value("${swagger.enable}")
    private boolean enableSwagger;



    @Bean
    public Docket stockApi() {
        String groupName = "堆场发货管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.stock"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket maBasicApi() {
        String groupName = "基础数据-计量单位";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.ma"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);

        return docket;
    }

    @Bean
    public Docket prodlineBasicApi() {
        String groupName = "基础数据-工序工位";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.prodline"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);

        return docket;
    }

    @Bean
    public Docket yardBasicApi() {
        String groupName = "基础数据-库区货架;质检物流";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.yard"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);

        return docket;
    }

    @Bean
    public Docket baMaApi() {
        String groupName = "原料管理-基础信息";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.ba"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket materialApi() {
        String groupName = "原料管理-出入库";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.material"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket sfgtApi() {
        String groupName = "半成品管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.sfg"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket projectPrApi() {
        String groupName = "项目管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.project"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket productPrApi() {
        String groupName = "构件管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.product"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket deptSysApi() {
        String groupName = "部门管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.dept"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket sysApi() {
        String groupName = "菜单;文件管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.sys"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket sysCodeSysApi() {
        String groupName = "字典管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.syscode"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket userSysApi() {
        String groupName = "用户管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.user"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket basicdataSysTemApi() {
        String groupName = "托盘;客户管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.basicdata"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket checkApi() {
        String groupName = "质检管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.check"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket poSystemApi() {
        String groupName = "订单管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.po"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket purchaseSystemApi() {
        String groupName = "采购管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.purchase"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }




    @Bean
    public Docket messageApi() {
        String groupName = "消息推送模版";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.message"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket reportInfoApi() {
        String groupName = "报表管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.report"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }

    @Bean
    public Docket dingDingInfoApi() {
        String groupName = "钉钉管理";
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo(groupName))
                //配置在生产环境和在测试环境是否开启
                .enable(enableSwagger)
                //使用菱形括号的情况下，Swagger将生成无效的规范，从而在Swagger编辑器中导致验证错误（“ $ ref值必须是RFC3986兼容的百分比编码URI”
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.smartpc.chiyun.controller.dingding"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .groupName(groupName);


        return docket;
    }
}
