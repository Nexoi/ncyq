package com.seeu.ywq.task.service.impl;

import com.seeu.ywq.globalconfig.service.TaskConfigurerService;
import com.seeu.ywq.task.model.DayFlushTask;
import com.seeu.ywq.task.repository.DayFlushTaskRepository;
import com.seeu.ywq.task.service.DayFlushTaskService;
import com.seeu.ywq.utils.DateFormatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DayFlushTaskServiceImpl implements DayFlushTaskService {

    @Resource
    private DayFlushTaskRepository repository;
    @Autowired
    private DateFormatterService dateFormatterService;
    @Autowired
    private TaskConfigurerService taskConfigurerService;

    @Async
    @Override
    public DayFlushTask update(Long uid, DayFlushTask.TYPE type) {
        //
        Long today = dateFormatterService.getyyyyMMdd(new Date());
        Integer totalProgress = 0;
        switch (type) {
            case like:
                totalProgress = taskConfigurerService.getTaskClickLikeProgress();
                break;
            case share:
                totalProgress = taskConfigurerService.getTaskShareProgress();
                break;
            case signin:
                totalProgress = taskConfigurerService.getTaskSignInProgress();
                break;
            case comment:
                totalProgress = taskConfigurerService.getTaskCommentProgress();
                break;
        }
        DayFlushTask task = repository.findByDayAndUidAndType(today, uid, type);
        if (task == null) {
            task = new DayFlushTask();
            task.setType(type);
            task.setUid(uid);
            task.setUpdateTime(new Date());
            task.setDay(today);
            task.setCurrentProgress(0);
            task.setTotalProgress(totalProgress);
        }
        if (null == task.getCurrentProgress()) task.setCurrentProgress(0);
        task.setCurrentProgress(task.getCurrentProgress() + 1);
        return repository.save(task);
    }

    @Override
    public List<DayFlushTask> list(Long uid, Date day) {
        Long today = dateFormatterService.getyyyyMMdd(day);
        return list(uid, today);
    }

    @Override
    public List<DayFlushTask> list(Long uid, Long day) {
        List<DayFlushTask> list = repository.findAllByUidAndDay(uid, day);
        if (list == null) list = new ArrayList<>();
        Map<DayFlushTask.TYPE, DayFlushTask> map = new HashMap();
        for (DayFlushTask task : list) {
            if (task == null) continue;
            map.put(task.getType(), task);
        }
        // check 4 task and flush pojo data
        List<DayFlushTask> flushTasks = new ArrayList<>();
        flushTasks.add(formDayFlushTask(DayFlushTask.TYPE.like, map));
        flushTasks.add(formDayFlushTask(DayFlushTask.TYPE.comment, map));
        flushTasks.add(formDayFlushTask(DayFlushTask.TYPE.share, map));
        flushTasks.add(formDayFlushTask(DayFlushTask.TYPE.signin, map));
        return flushTasks;
    }

    private DayFlushTask formDayFlushTask(DayFlushTask.TYPE type, Map<DayFlushTask.TYPE, DayFlushTask> map) {
        DayFlushTask task = map.get(type);
        if (task != null) {
            task.setUid(null);
            task.setId(null);
            Integer current = task.getCurrentProgress();
            Integer total = task.getTotalProgress();
            if (current > total) // 不能超过最大值
                task.setCurrentProgress(total);
            return task;
        }
        Date date = new Date();
        Integer totalProgress = 0;
        switch (type) {
            case like:
                totalProgress = taskConfigurerService.getTaskClickLikeProgress();
                break;
            case share:
                totalProgress = taskConfigurerService.getTaskShareProgress();
                break;
            case signin:
                totalProgress = taskConfigurerService.getTaskSignInProgress();
                break;
            case comment:
                totalProgress = taskConfigurerService.getTaskCommentProgress();
                break;
        }
        task = new DayFlushTask();
        task.setType(type);
        task.setUpdateTime(date);
        task.setDay(dateFormatterService.getyyyyMMdd(date));
        task.setTotalProgress(totalProgress);
        task.setCurrentProgress(0);
        return task;
    }
}
