package com.smartpc.chiyun.swapper;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置类
 */
public class BaseSwagger2Config {
    /**
     * 默认token名称
     */
    protected String tokenKey = "x-access-token";

    public BaseSwagger2Config() {
    }

    protected List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList(1);
        ApiKey apiKey = new ApiKey(this.tokenKey, this.tokenKey, "header");
        apiKeyList.add(apiKey);
        return apiKeyList;
    }

    protected List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList();
        securityContexts.add(SecurityContext.builder().securityReferences(this.defaultAuth()).forPaths(PathSelectors.any()).build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        List<SecurityReference> securityReferences = new ArrayList();
        securityReferences.add(new SecurityReference(this.tokenKey, authorizationScopes));
        return securityReferences;
    }
}
