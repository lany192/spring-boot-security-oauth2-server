package com.github.lany192.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "sys_account", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Account extends BaseEntity {

    private static final long serialVersionUID = -5519234457588411587L;
    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    List<RoleEntity> roles = new ArrayList<>();
    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String username;
    @Column(nullable = false)
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
    @Column(name = "failure_count", columnDefinition = "int default 0")
    private int failureCount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountOpenCode() {
        return accountOpenCode;
    }

    public void setAccountOpenCode(String accountOpenCode) {
        this.accountOpenCode = accountOpenCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(Date failureTime) {
        this.failureTime = failureTime;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }


}
