package com.seeu.ywq.task.repository;

import com.seeu.ywq.task.model.DayFlushTask;
import com.seeu.ywq.task.model.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DayFlushTaskRepository extends JpaRepository<DayFlushTask, Long> {
    DayFlushTask findByDayAndUidAndCategory(@Param("day") Long day, @Param("uid") Long uid, @Param("category") TaskCategory category);

    List<DayFlushTask> findAllByUidAndDay(@Param("uid") Long uid, @Param("day") Long day);
}
