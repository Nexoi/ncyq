package com.seeu.ywq.task.repository;

import com.seeu.ywq.task.model.StaticTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaticTaskRepository extends JpaRepository<StaticTask, Long> {
    StaticTask findByUidAndType(@Param("uid") Long uid, @Param("type") StaticTask.TYPE type);

    List<StaticTask> findAllByUid(@Param("uid") Long uid);
}
