package com.seeu.ywq._web.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_web_page_activity")
public class WebPageActivity {

//    public enum TYPE {
//        activity,
//        publish,
//        help
//    }

    public enum DELETE_FLAG {
        show,
        delete
    }

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated
    private DELETE_FLAG deleteFlag;

    private String title;
    @Column(length = Integer.MAX_VALUE)
    private String htmlContent;
    private Date updateTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DELETE_FLAG getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DELETE_FLAG deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
