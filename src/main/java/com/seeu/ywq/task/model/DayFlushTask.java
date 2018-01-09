package com.seeu.ywq.task.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

//@Entity
//@Table(name = "ywq_task_day")
public class DayFlushTask {
    public enum TYPE{
        signin,
        share,
        comment,
        like
    }

    private Long id;
    private TYPE type;
    private Integer currentProgress;
    @Transient
    private Integer totalProgress;

}
