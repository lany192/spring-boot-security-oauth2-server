package com.github.lany192.service.impl;

import com.github.dozermapper.core.Mapper;
import com.github.lany192.domain.ScopeDefinition;
import com.github.lany192.entity.ScopeEntity;
import com.github.lany192.repository.ScopeDefinitionRepository;
import com.github.lany192.exception.NotImplementException;
import com.github.lany192.service.ScopeDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScopeDefinitionServiceImpl implements ScopeDefinitionService {

    @Autowired
    ScopeDefinitionRepository scopeDefinitionRepository;

    @Autowired
    Mapper dozerMapper;

    @Override
    public ScopeDefinition findByScope(String scope) throws NotImplementException {
        ScopeEntity scopeEntity = scopeDefinitionRepository.findByScope(scope);
        if (scopeEntity != null) {
            return dozerMapper.map(scopeEntity, ScopeDefinition.class);
        } else {
            return null;
        }
    }

}