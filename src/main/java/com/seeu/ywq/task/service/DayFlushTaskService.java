package com.seeu.ywq.task.service;

import com.seeu.ywq.task.model.DayFlushTask;
import com.seeu.ywq.task.model.TaskCategory;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.List;

public interface DayFlushTaskService {
    @Async
    DayFlushTask update(Long uid, TaskCategory.CATEGORY categoryId);

    List<DayFlushTask> list(Long uid, Date day);

    /**
     *
     * @param uid
     * @param day 20180101
     * @return
     */
    List<DayFlushTask> list(Long uid, Long day);

}
