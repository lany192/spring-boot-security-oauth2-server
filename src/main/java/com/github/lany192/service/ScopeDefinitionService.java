package com.github.lany192.service;

import com.github.lany192.exception.NotImplementException;
import com.github.lany192.domain.ScopeDefinition;

public interface ScopeDefinitionService extends CommonServiceInterface<ScopeDefinition> {
    default ScopeDefinition findByScope(String scope) throws NotImplementException {
        throw new NotImplementException();
    }
}
