package com.seeu.ywq.task.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_task_day")
public class DayFlushTask {
    public enum TYPE {
        signin,
        share,
        comment,
        like
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 8)
    private Long day;// 20180101
    private Long uid;
    @Enumerated
    private TYPE type;
    private Integer currentProgress;
    private Integer totalProgress;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
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

    public Integer getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Integer currentProgress) {
        this.currentProgress = currentProgress;
    }

    public Integer getTotalProgress() {
        return totalProgress;
    }

    public void setTotalProgress(Integer totalProgress) {
        this.totalProgress = totalProgress;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}