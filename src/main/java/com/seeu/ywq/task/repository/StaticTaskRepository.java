package com.seeu.ywq.task.repository;

import com.seeu.ywq.task.model.StaticTask;
import com.seeu.ywq.task.model.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaticTaskRepository extends JpaRepository<StaticTask, Long> {
    StaticTask findByUidAndCategory(@Param("uid") Long uid, @Param("category") TaskCategory category);

    List<StaticTask> findAllByUid(@Param("uid") Long uid);
}
