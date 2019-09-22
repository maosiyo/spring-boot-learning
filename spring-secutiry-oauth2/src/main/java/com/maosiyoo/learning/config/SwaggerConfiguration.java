package com.maosiyoo.learning.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableSwaggerBootstrapUi;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
@Import(BeanValidatorPluginsConfiguration.class)
//@Profile({"dev"})
public class SwaggerConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 全局使用的，对于整个 Swagger2 应用有效
     * Swagger2 的安全配置 >>> 配置客户端信息
     *
     * @return Swagger2 的安全配置
     */
    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .appName("oauth2 认证测试应用")
                .clientId(AuthorizationServerConfiguration.CLIENT_ID)
                .clientSecret(AuthorizationServerConfiguration.CLIENT_SECRET)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    /**
     * 配置 API 分组的信息
     *
     * @return API 文档的详细信息
     */
    @Bean
    public Docket defaultRestApi() {
        String packageName = this.getClass().getPackage().getName();
        String basePackageName = packageName.substring(0, packageName.lastIndexOf("config")).concat("controller");
        logger.debug("basePackageName >>> {}", basePackageName);

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("测试分组")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackageName))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API 文档")
                .description("简单优雅的restful风格")
                .version("1.0.0")
                .build();
    }

    /**
     * 配置如何从认证服务器获取授权的方案
     *
     * @return 授权模式配置
     */
    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(AuthorizationServerConfiguration.AUTH_SERVER + "/token", "access_token"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint(AuthorizationServerConfiguration.AUTH_SERVER + "/authorize", AuthorizationServerConfiguration.CLIENT_ID, AuthorizationServerConfiguration.CLIENT_SECRET))
                .build();

        SecurityScheme oauth = new OAuthBuilder()
                .name("oauth2")
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations"),
                new AuthorizationScope("foo", "Access foo API"),
                new AuthorizationScope("app", "Access app API")};
        return scopes;
    }

    /**
     * 将具体的 API 与授权模式关联起来
     *
     * @return 安全上下文
     */
    private SecurityContext securityContext() {
        SecurityReference securityReference = SecurityReference.builder().reference("oauth2").scopes(scopes()).build();

        return SecurityContext.builder()
                .securityReferences(Lists.newArrayList(securityReference))
                .forPaths(PathSelectors.regex("/sitf.*"))
                .build();
    }

}
