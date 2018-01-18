package com.seeu.ywq.task.service.impl;

import com.seeu.ywq.exception.NewHerePackageReceiveEmptyException;
import com.seeu.ywq.globalconfig.service.TaskConfigurerService;
import com.seeu.ywq.task.model.DayFlushTask;
import com.seeu.ywq.task.model.StaticTask;
import com.seeu.ywq.task.model.TaskCategory;
import com.seeu.ywq.task.repository.StaticTaskRepository;
import com.seeu.ywq.task.service.StaticTaskService;
import com.seeu.ywq.task.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class StaticTaskServiceImpl implements StaticTaskService {
    @Resource
    private StaticTaskRepository repository;
    @Autowired
    private TaskCategoryService taskCategoryService;

    @Override
    public StaticTask update(Long uid, TaskCategory.CATEGORY categoryId) {
        TaskCategory category = taskCategoryService.findOne(categoryId);
        if (category == null)//
            return null;
        StaticTask task = repository.findByUidAndCategory(uid, category);
        if (task == null) {
            task = new StaticTask();
            task.setCategory(category);
            task.setUid(uid);
            task.setUpdateTime(new Date());
            task.setCurrentProgress(0);
            task.setTotalProgress(category.getTotalProgress());
        }
        if (null == task.getCurrentProgress()) task.setCurrentProgress(0);
        task.setCurrentProgress(task.getCurrentProgress() + 1);
        task.setUpdateTime(new Date());
        return repository.save(task);
    }

    @Override
    public List<StaticTask> list(Long uid) {
        List<StaticTask> list = repository.findAllByUid(uid);
        if (list == null) list = new ArrayList<>();
        // hash
        Map<TaskCategory.CATEGORY, StaticTask> map = new HashMap();
        for (StaticTask task : list) {
            map.put(task.getCategory().getCategory(), task);
        }
        // 匹配
        List<TaskCategory> taskCategories = taskCategoryService.findByType(TaskCategory.TYPE.LONGTIME);
        List<StaticTask> flushTasks = new ArrayList<>();
        for (TaskCategory category : taskCategories) {
            StaticTask task = map.get(category.getCategory());
            if (task == null) {
                task = new StaticTask();
                task.setCategory(category);
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


    /**
     * 领取新人礼包
     *
     * @param uid
     * @return
     */
    @Override
    public StaticTask receiveNewHerePackage(Long uid) throws NewHerePackageReceiveEmptyException {
        TaskCategory category = taskCategoryService.findOne(TaskCategory.CATEGORY.newpackage);
        StaticTask task = repository.findByUidAndCategory(uid, category);
        if (task == null) {
            task = new StaticTask();
//            task.setUid(uid);
//            task.setUpdateTime(new Date());
            task.setCurrentProgress(0);
            task.setTotalProgress(category.getTotalProgress());
        }
        if (task.getCurrentProgress() >= task.getTotalProgress()) // 表示不可以再领取了
            throw new NewHerePackageReceiveEmptyException(uid);
        //TODO many many things here (receive package to uid account)
        StaticTask staticTask = update(uid, TaskCategory.CATEGORY.newpackage);
        if (staticTask != null) {
            // 消除不必要显示的数据
            staticTask.setId(null);
            staticTask.setUid(null);
        }
        return staticTask;
    }
}
