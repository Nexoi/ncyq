package com.seeu.ywq.task.service;

import com.seeu.ywq.task.model.TaskCategory;

import java.util.List;

public interface TaskCategoryService {
    TaskCategory save(TaskCategory category);

    List<TaskCategory> save(List<TaskCategory> categories);

    TaskCategory findOne(TaskCategory.CATEGORY category);

    List<TaskCategory> findByType(TaskCategory.TYPE type);

    List<TaskCategory> findAll();


}
