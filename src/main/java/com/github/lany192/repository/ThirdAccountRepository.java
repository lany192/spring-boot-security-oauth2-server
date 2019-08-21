package com.github.lany192.repository;

import com.github.lany192.entity.ThirdAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdAccountRepository extends JpaRepository<ThirdAccount, Long> {
    ThirdAccount findByThirdPartyAndThirdPartyAccountId(String thirdParty, String thirdPartyAccountId);
}
