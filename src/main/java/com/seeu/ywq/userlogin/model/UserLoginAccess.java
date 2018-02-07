package com.seeu.ywq.userlogin.model;

import io.swagger.annotations.ApiParam;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ywq_user_login")
@DynamicUpdate
public class UserLoginAccess {
    public enum GENDER {
        male,
        female
    }

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uid;

    @ApiParam(hidden = true)
    @Column(length = 15)
    @Length(max = 15, message = "Phone Length could not larger than 15")
    private String phone;

    @ApiParam(hidden = true)
    @Column(length = 40)
    private String nickname;

    @ApiParam(hidden = true)
    @Enumerated
    private GENDER gender;

    @ApiParam(hidden = true)
    @Column(length = 400)
    private String headIconUrl;

    @ApiParam(hidden = true)
    private String password;

    @ApiParam(hidden = true)
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    @ApiParam(hidden = true)
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    //    @ApiParam(hidden = true)
//    private Long likeNum;   // 点赞人数（@see User.class）
    // 最近登陆坐标
    @ApiParam(hidden = true)
    @Column(name = "longitude", precision = 19, scale = 10)
    private BigDecimal longitude;// 经度
    @ApiParam(hidden = true)
    @Column(name = "latitude", precision = 19, scale = 10)
    private BigDecimal latitude; // 纬度

    @ApiParam(hidden = true)
    @Enumerated
    @Column(name = "member_status")
    private USER_STATUS memberStatus;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public GENDER getGender() {
        return gender;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public USER_STATUS getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(USER_STATUS memberStatus) {
        this.memberStatus = memberStatus;
    }

}