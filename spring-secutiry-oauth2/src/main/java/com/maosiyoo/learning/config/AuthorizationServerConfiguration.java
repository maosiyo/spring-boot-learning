package com.maosiyoo.learning.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String AUTH_SERVER = "http://localhost:8080/oauth";
    public static final String CLIENT_ID = "client";
    public static final String CLIENT_SECRET = "secret";

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * /oauth/token
     * /oauth/token_key
     * /oauth/check_token
     * 认证端点走的 basic 认证过滤器
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(AuthorizationServerConfiguration.CLIENT_ID)
                .secret(passwordEncoder.encode(AuthorizationServerConfiguration.CLIENT_SECRET))
                .authorizedGrantTypes("authorization_code")
                .scopes("app")
                .redirectUris("https//www.baidu.com",
                        "http://localhost:8080/webjars/springfox-swagger-ui/oauth2-redirect.html");
    }

}
