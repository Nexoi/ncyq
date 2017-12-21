package com.seeu.ywq.userlogin.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Table(name = "ywq_user_login", indexes = {
        @Index(name = "USERLOGIN_INDEX1", unique = true, columnList = "uid"),
        @Index(name = "USERLOGIN_INDEX2", unique = true, columnList = "phone"),
        @Index(name = "USERLOGIN_INDEX3", unique = false, columnList = "nickname"),
        @Index(name = "USERLOGIN_INDEX4", unique = false, columnList = "longitude"),
        @Index(name = "USERLOGIN_INDEX5", unique = false, columnList = "latitude"),
        @Index(name = "USERLOGIN_INDEX6", unique = false, columnList = "position_block_y"),
        @Index(name = "USERLOGIN_INDEX7", unique = false, columnList = "position_block_x")
})
@DynamicUpdate
public class UserLogin implements UserDetails {
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
    @Column(length = 20)
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

    @ApiParam(hidden = true)
    private Long likeNum;   // 点赞人数
    // 最近登陆坐标
    @ApiParam(hidden = true)
    @Column(name = "longitude", precision = 19, scale = 10)
    private BigDecimal longitude;// 经度
    @ApiParam(hidden = true)
    @Column(name = "latitude", precision = 19, scale = 10)
    private BigDecimal latitude; // 纬度

    @ApiParam(hidden = true)
    @Column(name = "position_block_y")
    private Long positionBlockY; // 地区标记，1 km 间距，纬度

    @ApiParam(hidden = true)
    @Column(name = "position_block_x")
    private Long positionBlockX; // 地区标记，1 km 间距，经度

    @ApiParam(hidden = true)
    @Enumerated
    @Column(name = "member_status")
    private USER_STATUS memberStatus;

    @ApiParam(hidden = true)
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

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public GENDER getGender() {
        return gender;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public Long getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Long likeNum) {
        this.likeNum = likeNum;
    }

    public Long getPositionBlockY() {
        return positionBlockY;
    }

    public void setPositionBlockY(Long positionBlockY) {
        this.positionBlockY = positionBlockY;
    }

    public Long getPositionBlockX() {
        return positionBlockX;
    }

    public void setPositionBlockX(Long positionBlockX) {
        this.positionBlockX = positionBlockX;
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
        return this.phone;
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