package com.seeu.ywq.userlogin.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * third part user login
 */
@Entity
@Table(name = "ywq_user_login_third", indexes = {
        @Index(name = "thirdLogin_Index1", columnList = "token"),
        @Index(name = "thirdLogin_Index2", columnList = "type"),
        @Index(name = "thirdLogin_Index3", columnList = "ywq_uid")
})
public class ThirdUserLogin {
    public enum TYPE {
        WeChat,
        Weibo,
        QQ
    }

    @Id
    @Column(length = 30)
    @NotNull
    private String name; // like uid
    @Enumerated
    @Column(name = "type")
    private TYPE type;
    @NotNull
    private String credential;
    @Column(name = "token")
    private String token;
    private String nickName;
    //..
    @NotNull
    @Column(name = "ywq_uid")
    private Long ywqUid;
    private Date updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getYwqUid() {
        return ywqUid;
    }

    public void setYwqUid(Long ywqUid) {
        this.ywqUid = ywqUid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
