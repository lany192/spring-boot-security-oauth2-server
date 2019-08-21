package com.github.lany192.service.impl;

import com.github.lany192.config.CachesEnum;
import com.github.lany192.exception.InvalidClientException;
import com.github.lany192.entity.OauthClient;
import com.github.lany192.repository.OauthClientRepository;
import com.github.lany192.exception.AlreadyExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    OauthClientRepository oauthClientRepository;

    @Autowired
    CacheManager cacheManager;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        Cache.ValueWrapper valueWrapper = cacheManager.getCache(CachesEnum.Oauth2ClientCache.name()).get(clientId);

        if (valueWrapper != null) {
            return (ClientDetails) valueWrapper.get();
        }

        OauthClient oauthClient = oauthClientRepository.findByClientId(clientId);
        if (oauthClient != null) {
            if (oauthClient.getRecordStatus() < 0) {
                throw new InvalidClientException(String.format("clientId %s is disabled!", clientId));
            }
            if (oauthClient.getExpirationDate() != null && oauthClient.getExpirationDate().compareTo(new Date()) < 0) {
                throw new AlreadyExpiredException(String.format("clientId %s already expired!", clientId));
            }
            BaseClientDetails baseClientDetails = new BaseClientDetails();
            baseClientDetails.setClientId(oauthClient.getClientId());
            if (!StringUtils.isEmpty(oauthClient.getResourceIds())) {
                baseClientDetails.setResourceIds(StringUtils.commaDelimitedListToSet(oauthClient.getResourceIds()));
            }
            baseClientDetails.setClientSecret(oauthClient.getClientSecret());
            if (!StringUtils.isEmpty(oauthClient.getScope())) {
                baseClientDetails.setScope(StringUtils.commaDelimitedListToSet(oauthClient.getScope()));
            }
            if (!StringUtils.isEmpty(oauthClient.getAuthorizedGrantTypes())) {
                baseClientDetails.setAuthorizedGrantTypes(StringUtils.commaDelimitedListToSet(oauthClient.getAuthorizedGrantTypes()));
            } else {
                baseClientDetails.setAuthorizedGrantTypes(StringUtils.commaDelimitedListToSet("authorization_code"));
            }
            if (!StringUtils.isEmpty(oauthClient.getWebServerRedirectUri())) {
                baseClientDetails.setRegisteredRedirectUri(StringUtils.commaDelimitedListToSet(oauthClient.getWebServerRedirectUri()));
            }
            if (!StringUtils.isEmpty(oauthClient.getAuthorities())) {
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                StringUtils.commaDelimitedListToSet(oauthClient.getAuthorities()).forEach(s -> authorities.add(new SimpleGrantedAuthority(s)));
                baseClientDetails.setAuthorities(authorities);
            }
            if (oauthClient.getAccessTokenValidity() != null && oauthClient.getAccessTokenValidity() > 0) {
                baseClientDetails.setAccessTokenValiditySeconds(oauthClient.getAccessTokenValidity());
            }
            if (oauthClient.getRefreshTokenValidity() != null && oauthClient.getRefreshTokenValidity() > 0) {
                baseClientDetails.setRefreshTokenValiditySeconds(oauthClient.getRefreshTokenValidity());
            }
///            baseClientDetails.setAdditionalInformation(oauthClientEntity.getAdditionalInformation());
            if (!StringUtils.isEmpty(oauthClient.getAutoApprove())) {
                baseClientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(oauthClient.getAutoApprove()));
            }
            cacheManager.getCache(CachesEnum.Oauth2ClientCache.name()).put(clientId, baseClientDetails);
            return baseClientDetails;
        } else {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }
}
