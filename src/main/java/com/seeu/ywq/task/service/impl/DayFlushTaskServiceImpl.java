package com.seeu.ywq.task.service.impl;

import com.seeu.ywq.event_listener.publish_react.ShareEvent;
import com.seeu.ywq.event_listener.task.SignInTodayEvent;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.exception.NewHerePackageReceiveEmptyException;
import com.seeu.ywq.exception.SignInTodayAlreadyFinishedException;
import com.seeu.ywq.globalconfig.service.TaskConfigurerService;
import com.seeu.ywq.task.model.DayFlushTask;
import com.seeu.ywq.task.model.StaticTask;
import com.seeu.ywq.task.model.TaskCategory;
import com.seeu.ywq.task.repository.DayFlushTaskRepository;
import com.seeu.ywq.task.service.DayFlushTaskService;
import com.seeu.ywq.task.service.TaskCategoryService;
import com.seeu.ywq.utils.DateFormatterService;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
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
    private TaskCategoryService taskCategoryService;
    @Autowired
    private ApplicationContext applicationContext;

    @Async
    @Override
    public DayFlushTask update(Long uid, TaskCategory.CATEGORY categoryId) {
        //
        Long today = dateFormatterService.getyyyyMMdd(new Date());
        TaskCategory category = taskCategoryService.findOne(categoryId);
        if (category == null)//
            return null;
        DayFlushTask task = repository.findByDayAndUidAndCategory(today, uid, category);
        if (task == null) {
            task = new DayFlushTask();
            task.setCategory(category);
            task.setUid(uid);
            task.setUpdateTime(new Date());
            task.setDay(today);
            task.setCurrentProgress(0);
            task.setTotalProgress(category.getTotalProgress());
        }
        if (null == task.getCurrentProgress()) task.setCurrentProgress(0);
        task.setCurrentProgress(task.getCurrentProgress() + 1);
        task.setUpdateTime(new Date());
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
        // hash
        Map<TaskCategory.CATEGORY, DayFlushTask> map = new HashMap();
        for (DayFlushTask task : list) {
            map.put(task.getCategory().getCategory(), task);
        }
        // 匹配
        List<TaskCategory> taskCategories = taskCategoryService.findByType(TaskCategory.TYPE.DAYFLUSH);
        List<DayFlushTask> flushTasks = new ArrayList<>();
        for (TaskCategory category : taskCategories) {
            DayFlushTask task = map.get(category.getCategory());
            if (task == null) {
                task = new DayFlushTask();
                task.setCategory(category);
                task.setDay(day);
                task.setCurrentProgress(0);
                task.setUpdateTime(new Date());
            }
            task.setTotalProgress(category.getTotalProgress());
            // 消去字段
            category.setTotalProgress(null);
            task.setId(null);
            task.setUid(null);
            // 最大值设定
            Integer current = task.getCurrentProgress();
            Integer total = task.getTotalProgress();
            if (current > total) // 不能超过最大值
                task.setCurrentProgress(total);
            flushTasks.add(task);
        }
        return flushTasks;
    }

    @Override
    public DayFlushTask signToday(Long uid) throws SignInTodayAlreadyFinishedException {
        Long today = dateFormatterService.getyyyyMMdd(new Date());
        TaskCategory category = taskCategoryService.findOne(TaskCategory.CATEGORY.signin);
        DayFlushTask task = repository.findByDayAndUidAndCategory(today, uid, category);
        if (task == null) {
            task = new DayFlushTask();
//            task.setUid(uid);
//            task.setDay(today);
//            task.setUpdateTime(new Date());
            task.setCurrentProgress(0);
            task.setTotalProgress(category.getTotalProgress());
        }
        if (task.getCurrentProgress() >= task.getTotalProgress()) // 表示不可以再领取了
            throw new SignInTodayAlreadyFinishedException(uid);
        DayFlushTask dayFlushTask = update(uid, TaskCategory.CATEGORY.signin);
        if (dayFlushTask != null) {
            // 消除不必要显示的数据
            dayFlushTask.setId(null);
            dayFlushTask.setUid(null);
        }
        // 通知
        applicationContext.publishEvent(new SignInTodayEvent(this, uid));
        return dayFlushTask;
    }
}
