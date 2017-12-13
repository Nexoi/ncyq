package com.seeu.shopper.user.model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_login", indexes = {
        @Index(name = "USERLOGIN_INDEX1", unique = true, columnList = "uid"),
        @Index(name = "USERLOGIN_INDEX2", unique = true, columnList = "email")
})
@DynamicUpdate
public class UserLogin implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uid;

    @Column(length = 45)
    @Length(max = 45,message = "Email Length could not larger than 45")
    @Email
    private String email;

    private String password;

    @Column(name = "last_login_ip")
    private String lastLoginIp;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * -2 未激活
     * -1 注销
     * 1 正常
     * 2 违规
     * 4 重要客户
     */
    @Enumerated
    @Column(name = "member_status")
    private USER_STATUS memberStatus;

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<UserAuthRole> roles;

    /**
     * @return uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return last_login_ip
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * @param lastLoginIp
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * @return last_login_time
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public USER_STATUS getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(USER_STATUS memberStatus) {
        this.memberStatus = memberStatus;
    }


    // 以下是权限验证块


    public List<UserAuthRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserAuthRole> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        List<UserAuthRole> roles = this.getRoles();
        for (UserAuthRole role : roles) {
            auths.add(new SimpleGrantedAuthority(role.getName()));
        }
        return auths;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}