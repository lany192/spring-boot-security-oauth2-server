package com.github.lany192.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
@Setter
@Getter
public class UserInfo extends User {
    private String accountOpenCode;
    private String nickname;

    public UserInfo(String accountOpenCode, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(accountOpenCode, username, password, true, true, true, true, authorities);
    }

    public UserInfo(String accountOpenCode, String username, String password, boolean enabled,
                    boolean accountNonExpired, boolean credentialsNonExpired,
                    boolean accountNonLocked,
                    Collection<? extends GrantedAuthority> authorities)
        throws IllegalArgumentException {
        super(username, password, enabled, accountNonExpired,
            credentialsNonExpired, accountNonLocked, authorities);
        this.accountOpenCode = accountOpenCode;
    }
}
