package com.github.lany192.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginHistory extends BaseDomain {
    /**
     *
     */
    private static final long serialVersionUID = -3503838536778480869L;
    private String clientId;
    private String username;
    private String ip;
    private String device;
}
