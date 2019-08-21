package com.github.lany192.service;

import com.github.lany192.exception.NotImplementException;
import com.github.lany192.domain.OauthClient;

public interface OauthClientService extends CommonServiceInterface<OauthClient> {
    default OauthClient findByClientId(String clientId) {
        throw new NotImplementException();
    }
}
