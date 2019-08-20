package com.github.lany192.persistence.repository;

import com.github.lany192.persistence.entity.LoginHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long> {
    Page<LoginHistoryEntity> findByUsername(String username, Pageable page);
}
