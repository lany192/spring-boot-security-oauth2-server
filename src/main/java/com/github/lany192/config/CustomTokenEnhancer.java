package com.github.lany192.config;

import com.github.lany192.domain.UserInfo;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {

    /**
     * 自定义一些token属性
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        // 注意!,client_credentials模式没有用户!
        if (authentication.getUserAuthentication() != null) {
            // 与登录时候放进去的UserDetail实现类一致
            UserInfo user = (UserInfo) authentication.getUserAuthentication().getPrincipal();
            map.put("grantType", authentication.getOAuth2Request().getGrantType());
            map.put("accountOpenCode", user.getAccountOpenCode());
            map.put("sub", user.getUsername());
            map.put("status", 1);
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
        }
        return accessToken;
    }

}
