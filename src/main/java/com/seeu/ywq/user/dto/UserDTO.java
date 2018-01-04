package com.seeu.ywq.user.dto;

import com.seeu.ywq.user.model.User;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class UserDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDay;  // 出生日期间
    private Integer height; // 身高
    @Column(precision = 6, scale = 2)
    private BigDecimal weight;  // 体重 Kg
    private Integer bust;       // 胸围
    private Integer waist;      // 腰围
    private Integer hip;        // 臀围
    @Enumerated
    private User.STAR_SIGN starSign; // 星座

    @Column(length = 400)
    private String introduce;   // 个人简介

    @Column(length = 45)
    private String wechat;

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getBust() {
        return bust;
    }

    public void setBust(Integer bust) {
        this.bust = bust;
    }

    public Integer getWaist() {
        return waist;
    }

    public void setWaist(Integer waist) {
        this.waist = waist;
    }

    public Integer getHip() {
        return hip;
    }

    public void setHip(Integer hip) {
        this.hip = hip;
    }

    public User.STAR_SIGN getStarSign() {
        return starSign;
    }

    public void setStarSign(User.STAR_SIGN starSign) {
        this.starSign = starSign;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
}
