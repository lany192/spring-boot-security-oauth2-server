package com.github.lany192.repository;

import com.github.lany192.entity.OauthClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthClientRepository extends JpaRepository<OauthClient, Long> {
    OauthClient findByClientId(String clientId);
}
