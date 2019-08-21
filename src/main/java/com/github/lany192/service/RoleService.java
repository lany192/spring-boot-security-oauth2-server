package com.github.lany192.service;

import com.github.lany192.exception.NotImplementException;
import com.github.lany192.domain.Role;

public interface RoleService extends CommonServiceInterface<Role> {
    default Role findByRoleName(String roleName) throws NotImplementException {
        throw new NotImplementException();
    }
}
