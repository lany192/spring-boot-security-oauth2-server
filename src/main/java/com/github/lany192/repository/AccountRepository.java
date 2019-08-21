package com.github.lany192.repository;

import com.github.lany192.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    Page<Account> findByUsernameLike(String username, Pageable page);

    boolean existsByUsername(String username);
}
