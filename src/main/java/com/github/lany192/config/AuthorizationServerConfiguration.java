package com.github.lany192.config;

import com.github.lany192.granter.SmsCodeTokenGranter;
import com.github.lany192.granter.WeChatMiniProgramTokenGranter;
import com.github.lany192.repository.RoleRepository;
import com.github.lany192.repository.ThirdAccountRepository;
import com.github.lany192.service.CaptchaService;
import com.github.lany192.service.impl.ClientDetailsServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Import(AuthorizationServerEndpointsConfiguration.class)
@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter implements InitializingBean {
    @Autowired
    ClientDetailsServiceImpl clientDetailsService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    CaptchaService captchaService;
    @Autowired
    ThirdAccountRepository thirdAccountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Value("${jwt.jks.keypass:keypass}")
    private String keypass;

    @Value("${thirdparty.weixin.mini.appid:1}")
    private String appId;

    @Value("${thirdparty.weixin.mini.secret:1}")
    private String secret;

    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"),
            "keypass".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt");
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setKeyPair(keyPair());

        // 测试用,资源服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
        /// accessTokenConverter.setSigningKey("123");
        return accessTokenConverter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // CustomTokenEnhancer 是我自定义一些数据放到token里用的
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), accessTokenConverter()));
        return tokenEnhancerChain;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 可以用redis等存储
     *
     * @return ApprovalStore
     */
    @Bean
    public ApprovalStore approvalStore() {
        TokenApprovalStore approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(tokenStore());
        return approvalStore;
    }

    @Bean
    public DefaultOAuth2RequestFactory oAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    @Bean
    public UserApprovalHandler userApprovalHandler() {
        ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
        handler.setApprovalStore(approvalStore());
        handler.setClientDetailsService(clientDetailsService);
        handler.setRequestFactory(oAuth2RequestFactory());
        return handler;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // 开启密码授权类型,注入authenticationManager来支持password模式
        endpoints.authenticationManager(authenticationManager);
        endpoints.accessTokenConverter(accessTokenConverter());
        endpoints.tokenStore(tokenStore());
        //endpoints.approvalStore(approvalStore);
        // !!!要使用refresh_token的话，需要额外配置userDetailsService!!!
        endpoints.userDetailsService(userDetailsService);
        endpoints.reuseRefreshTokens(true);
        endpoints.tokenGranter(new CompositeTokenGranter(getTokenGranters()));
        endpoints.authorizationCodeServices(authorizationCodeServices());
        // 设了 tokenGranter 后该配制失效,需要在 tokenServices() 中设置
///        endpoints.tokenEnhancer(tokenEnhancerChain);
        endpoints.userApprovalHandler(userApprovalHandler());
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        //自定义登录或者鉴权失败时的返回信息
        //endpoints.exceptionTranslator(webResponseExceptionTranslator);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
            .checkTokenAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
            .allowFormAuthenticationForClients();
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        // 使用默认
        return new InMemoryAuthorizationCodeServices();
    }

    @Bean
    public DefaultTokenServices authorizationServerTokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(tokenStore());
        services.setSupportRefreshToken(true);
        //token 有效时间,默认12h
        services.setClientDetailsService(clientDetailsService);
        // 如果没有设置它,JWT就失效了.
        services.setTokenEnhancer(tokenEnhancer());
        return services;
    }

    private List<TokenGranter> getTokenGranters() {
        List<TokenGranter> granters = new ArrayList<>();
        granters.add(new AuthorizationCodeTokenGranter(authorizationServerTokenServices(),
            authorizationCodeServices(), clientDetailsService, oAuth2RequestFactory()));
        granters.add(new RefreshTokenGranter(authorizationServerTokenServices(), clientDetailsService,
            oAuth2RequestFactory()));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(authorizationServerTokenServices(),
            clientDetailsService, oAuth2RequestFactory());
        granters.add(implicit);
        granters.add(new ClientCredentialsTokenGranter(authorizationServerTokenServices(), clientDetailsService,
            oAuth2RequestFactory()));
        if (authenticationManager != null) {
            granters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager,
                authorizationServerTokenServices(), clientDetailsService, oAuth2RequestFactory()));
        }

        granters.add(new SmsCodeTokenGranter(userDetailsService, authorizationServerTokenServices(),
            clientDetailsService, oAuth2RequestFactory(), captchaService));

        granters.add(new WeChatMiniProgramTokenGranter(thirdAccountRepository, roleRepository, authorizationServerTokenServices(),
            clientDetailsService, oAuth2RequestFactory(), appId, secret));
        return granters;
    }

    @Override
    public void afterPropertiesSet() {

    }

}
