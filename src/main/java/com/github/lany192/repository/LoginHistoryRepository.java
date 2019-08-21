package com.github.lany192.repository;

import com.github.lany192.entity.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    Page<LoginHistory> findByUsername(String username, Pageable page);
}
