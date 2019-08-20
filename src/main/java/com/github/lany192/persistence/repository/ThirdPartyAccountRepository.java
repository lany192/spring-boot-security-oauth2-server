package com.github.lany192.persistence.repository;

import com.github.lany192.persistence.entity.ThirdPartyAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdPartyAccountRepository extends JpaRepository<ThirdPartyAccountEntity, Long> {
    ThirdPartyAccountEntity findByThirdPartyAndThirdPartyAccountId(String thirdParty, String thirdPartyAccountId);
}
