package com.seeu.ywq.message.model;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.util.Date;

/**
 * 私人消息，点咱/评论/私信等
 */
@Entity
@Table(name = "ywq_message_personal", indexes = {
        @Index(name = "message_personal_index1", columnList = "create_time"),
        @Index(name = "message_personal_index2", columnList = "uid")
})
public class PersonalMessage {
    public enum TYPE {
        like,
        comment,
        reward,  // 收到打赏
        gift,     // 收到送礼
        yellowPicture, // 发送了黄图
    }//...

    @Id
    private Long id;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "uid")
    private Long uid;
    @Enumerated
    private TYPE type;
    @Column(length = Integer.MAX_VALUE)
    private String extraJson;

    @Transient
    private JSONObject json;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getExtraJson() {
        return extraJson;
    }

    public void setExtraJson(String extraJson) {
        this.extraJson = extraJson;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
