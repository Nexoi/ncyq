package com.seeu.ywq.task.repository;

import com.seeu.ywq.task.model.DayFlushTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DayFlushTaskRepository extends JpaRepository<DayFlushTask, Long> {
    DayFlushTask findByDayAndUidAndType(@Param("day") Long day, @Param("uid") Long uid, @Param("type") DayFlushTask.TYPE type);

    List<DayFlushTask> findAllByUidAndDay(@Param("uid") Long uid, @Param("day") Long day);
}
