package com.seeu.ywq.task.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_task_static")
public class StaticTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long uid;
    @JoinColumn(
            name = "category_id",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT)
    )//注意这里的@ForeignKey是javax.persistence包中类，不要使用org.hibernate的
    @ManyToOne
    private TaskCategory category;
    private Integer currentProgress;
    private Integer totalProgress;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }
}
