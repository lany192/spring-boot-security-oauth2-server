package com.github.lany192.service.impl;

import com.github.dozermapper.core.Mapper;
import com.github.lany192.entity.RoleEntity;
import com.github.lany192.repository.RoleRepository;
import com.github.lany192.exception.NotImplementException;
import com.github.lany192.domain.Role;
import com.github.lany192.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    Mapper dozerMapper;

    @Override
    public Role findByRoleName(String roleName) throws NotImplementException {
        RoleEntity roleEntity = roleRepository.findByRoleName(roleName);
        if (roleEntity != null) {
            return dozerMapper.map(roleEntity, Role.class);
        } else {
            return null;
        }
    }

}
