package com.seeu.ywq.userlogin.dvo;

import com.seeu.ywq.userlogin.model.USER_STATUS;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

public class UserLoginVO {
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
    private UserLogin.GENDER gender;

    @ApiParam(hidden = true)
    @Column(length = 400)
    private String headIconUrl;

    @ApiParam(hidden = true)
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    @ApiParam(hidden = true)
    @Column(name = "last_login_time")
    private Date lastLoginTime;

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

    public UserLogin.GENDER getGender() {
        return gender;
    }

    public void setGender(UserLogin.GENDER gender) {
        this.gender = gender;
    }

}
