package com.github.lany192.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Setter
@Getter
public class UserAccount extends BaseDomain {
    private String username;
    @JsonIgnore
    private String password;
    /**
     * 多种登陆方式合并账号使用
     */
    private String accountOpenCode;
    private String nickName;
    private String avatarUrl;
    private String email;
    private String mobile;
    private String province;
    private String city;
    private String address;
    private Date birthday;
    private String gender;
    private Date failureTime;
    private int failureCount;
    private List<Role> roles = new ArrayList<>();
}
