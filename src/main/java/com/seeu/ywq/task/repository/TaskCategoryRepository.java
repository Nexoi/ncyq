package com.seeu.ywq.task.repository;

import com.seeu.ywq.task.model.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, TaskCategory.CATEGORY> {
    List<TaskCategory> findAllByType(@Param("type") TaskCategory.TYPE type);
}
