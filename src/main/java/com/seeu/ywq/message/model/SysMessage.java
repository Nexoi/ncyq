package com.seeu.ywq.message.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统通知，每个人都能收到
 */

@Entity
@Table(name = "ywq_message_system")
public class SysMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date createTime;
    @Column(length = 400)
    private String text;
    @Column(length = 400)
    private String linkUrl;
    @Column(length = Integer.MAX_VALUE)
    private String extraJson;

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
}
