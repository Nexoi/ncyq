package com.seeu.ywq.message.model;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统通知，每个人都能收到
 */

@Entity
@Table(name = "ywq_message_system", indexes = {
        @Index(name = "message_sys_index1", columnList = "create_time")
})
public class SysMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "create_time")
    private Date createTime;
    @Column(length = 400)
    private String text;
    @Column(length = 400)
    private String linkUrl;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
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
