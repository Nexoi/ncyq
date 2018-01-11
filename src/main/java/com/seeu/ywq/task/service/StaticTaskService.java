package com.seeu.ywq.task.service;

import com.seeu.ywq.exception.NewHerePackageReceiveEmptyException;
import com.seeu.ywq.task.model.StaticTask;
import com.seeu.ywq.task.model.TaskCategory;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.List;

public interface StaticTaskService {
    @Async
    StaticTask update(Long uid, TaskCategory.CATEGORY categoryId);

    List<StaticTask> list(Long uid);

    StaticTask receiveNewHerePackage(Long uid)throws NewHerePackageReceiveEmptyException;
}
