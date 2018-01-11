package com.seeu.ywq.task.service.impl;

import com.seeu.ywq.task.model.TaskCategory;
import com.seeu.ywq.task.repository.TaskCategoryRepository;
import com.seeu.ywq.task.service.TaskCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskCategoryServiceImpl implements TaskCategoryService {
    @Resource
    private TaskCategoryRepository repository;

    @Override
    public TaskCategory save(TaskCategory category) {
        return repository.save(category);
    }

    @Override
    public List<TaskCategory> save(List<TaskCategory> categories) {
        return repository.save(categories);
    }

    @Override
    public TaskCategory findOne(TaskCategory.CATEGORY category) {
        return repository.findOne(category);
    }

    @Override
    public List<TaskCategory> findByType(TaskCategory.TYPE type) {
        return repository.findAllByType(type);
    }

    @Override
    public List<TaskCategory> findAll() {
        return repository.findAll();
    }
}
