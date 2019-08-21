package com.github.lany192.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScopeDefinition extends BaseDomain {
    private String scope;
    /**
     * 定义 解释
     */
    private String definition;
}
