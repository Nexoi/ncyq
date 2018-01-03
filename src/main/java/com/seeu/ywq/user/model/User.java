package com.seeu.ywq.user.model;

import io.swagger.annotations.ApiParam;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ywq_user")
@DynamicUpdate
@DynamicInsert
public class User {
    public enum STAR_SIGN {
        Aries,      // 白羊座
        Taurus,     // 金牛座
        Gemini,     // 双子座
        Cancer,     //  巨蟹座
        Leo,        //  狮子座
        Virgo,      //  处女座
        Libra,      //  天秤座
        Scorpio,    //  天蝎座
        Sagittarius,  //  射手座
        Capricorn,    //  摩羯座
        Aquarius,     //  水瓶座
        Pisces,       //  双鱼座
    }

    @ApiParam(hidden = true)
    @Id
    private Long uid;
    @ApiParam(hidden = true)
    private Long fansNum;   // 粉丝数
    @ApiParam(hidden = true)
    private Long followNum; // 关注人数
    @ApiParam(hidden = true)
    private Long publishNum; // 动态发布数

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDay;  // 出生日期间
    private Integer height; // 身高
    @Column(precision = 6, scale = 2)
    private BigDecimal weight;  // 体重 Kg
    private Integer bust;       // 胸围
    private Integer waist;      // 腰围
    private Integer hip;        // 臀围
    @Enumerated
    private STAR_SIGN starSign; // 星座

    @Column(length = 400)
    private String introduce;   // 个人简介

    @Column(length = 45)
    private String wechat;
    @ApiParam(hidden = true)
    @Column(length = 15)
    private String phone;       // 初始化的时候绑定为注册时使用的那个

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tags;
//    删除，独立在新表里
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Identification> identifications;

//    删除，废弃该表
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Skill> skills;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getFansNum() {
        return fansNum;
    }

    public void setFansNum(Long fansNum) {
        this.fansNum = fansNum;
    }

    public Long getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Long followNum) {
        this.followNum = followNum;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public STAR_SIGN getStarSign() {
        return starSign;
    }

    public void setStarSign(STAR_SIGN starSign) {
        this.starSign = starSign;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Long getPublishNum() {
        return publishNum;
    }

    public void setPublishNum(Long publishNum) {
        this.publishNum = publishNum;
    }
}
