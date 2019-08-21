package com.github.lany192.service.impl;

import com.github.lany192.domain.UserInfo;
import com.github.lany192.entity.RoleEntity;
import com.github.lany192.entity.Account;
import com.github.lany192.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userAccountRepository.findByUsername(username);
        if (account != null) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            if (account.getRoles() != null && account.getRoles().size() > 0) {
                for (RoleEntity temp : account.getRoles()) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(temp.getRoleName());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
            return new UserInfo(account.getAccountOpenCode(), account.getUsername(), account.getPassword(),
                account.getRecordStatus() >= 0, true, true, account.getRecordStatus() != -2, grantedAuthorities);
        } else {
            throw new UsernameNotFoundException(username + " not found!");
        }
    }
}
