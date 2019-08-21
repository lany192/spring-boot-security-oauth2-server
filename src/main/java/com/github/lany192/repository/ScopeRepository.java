package com.github.lany192.repository;

import com.github.lany192.entity.ScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScopeRepository extends JpaRepository<ScopeEntity, Long> {
    ScopeEntity findByScope(String scope);
}
