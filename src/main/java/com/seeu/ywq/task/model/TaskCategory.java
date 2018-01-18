package com.seeu.ywq.task.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_task_category")
public class TaskCategory {

    public enum CATEGORY {
        share(true),
        signin(true),
        comment(true),
        like(true),
        newpackage(false);

        private boolean isDayFlush;

        CATEGORY(boolean isDayFlush){
            this.isDayFlush = isDayFlush;
        }

        public boolean isDayFlush(){
            return isDayFlush;
        }
    }

    public enum TYPE {
        DAYFLUSH,
        LONGTIME
    }

    @Id
    @Enumerated
    private CATEGORY category;
    @Enumerated
    private TYPE type;
    private Integer totalProgress;
    private Long coin;
    private String title;
    private String subTitle;
    private Date updateTime;

    public CATEGORY getCategory() {
        return category;
    }

    public void setCategory(CATEGORY category) {
        this.category = category;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public Integer getTotalProgress() {
        return totalProgress;
    }

    public void setTotalProgress(Integer totalProgress) {
        this.totalProgress = totalProgress;
    }

    public Long getCoin() {
        return coin;
    }

    public void setCoin(Long coin) {
        this.coin = coin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
