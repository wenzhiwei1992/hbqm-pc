package com.smartpc.chiyun.config.security;

import com.smartpc.chiyun.config.security.service.PCUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationContext applicationContext;


    @Autowired
    private PcAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private PCUserDetailsService pcUserDetailsService;


    public SecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the ROLE_ prefix
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(pcUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 搜寻 匿名标记 url： PreAuthorize("hasAnyRole('anonymous')") 和 PreAuthorize("@pc.check('anonymous')") 和 AnonymousAccess
//		Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
//		Set<String> anonymousUrls = new HashSet<>();
//		for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
//			HandlerMethod handlerMethod = infoEntry.getValue();
//			AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
//			PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
//			if (null != preAuthorize && preAuthorize.value().toLowerCase().contains("anonymous")) {
//				anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
//			} else if (null != anonymousAccess && null == preAuthorize) {
//				anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
//			}
//		}

        http.authorizeRequests()
                // 静态资源等等
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webSocket/**"
                ).permitAll()
                .antMatchers(
                        "/user/login/**",
                        "/user/logout/**", "/productDetailPrintController/getProductDetailInfoById",
                        "/user/m2ep/buildSsoUrl",
                        "/user/m2ep/sso",
                        "/user/m2ep/ssodd",
                        "/sys/menu/getMenuList",
                        "/swagger-resources/**",
                        "/*/api-docs",
                        "/*/api-docs-ext",
                        "/webjars/**",
                        "/fileUpload/**",
                        "/doc.html",
                        "/invoice/findInvoiceChkInfo",
                        "/indexMap/**",
                        "/basicdata/department/findDepartmentBylevel/**",
                        "/basicdata/codeExplainContent/getByCodeNo/**",
                        "/basicdata/department/findDepartmentsByParentId",
                        "/org/add",
                        "/statistic/getIndexMapOrg"
                ).permitAll()
                // 阿里巴巴 druid
                .antMatchers("/druid/**", "/favicon.ico").permitAll()
                // 自定义匿名访问所有url放行 ： 允许 匿名和带权限以及登录用户访问
                //.antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
                .and().cors()
                .and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
        http.addFilterBefore(new MyFilterSecurityInterceptor(), UsernamePasswordAuthenticationFilter.class);
    }

}
