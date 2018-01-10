package com.seeu.ywq.task.service.impl;

import com.seeu.ywq.exception.NewHerePackageReceiveEmptyException;
import com.seeu.ywq.globalconfig.service.TaskConfigurerService;
import com.seeu.ywq.task.model.StaticTask;
import com.seeu.ywq.task.repository.StaticTaskRepository;
import com.seeu.ywq.task.service.StaticTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class StaticTaskServiceImpl implements StaticTaskService {
    @Resource
    private StaticTaskRepository repository;
    @Autowired
    private TaskConfigurerService taskConfigurerService;

    @Override
    public StaticTask update(Long uid, StaticTask.TYPE type) {
        if (type == null || type != StaticTask.TYPE.xinrenlibao)
            return null;
        Integer totalProgress = 0;
        switch (type) {
            case xinrenlibao:
                totalProgress = taskConfigurerService.getTaskNewHerePackageNumber();
                break;
        }
        StaticTask task = repository.findByUidAndType(uid, type);
        if (task == null) {
            task = new StaticTask();
            task.setType(type);
            task.setUid(uid);
            task.setUpdateTime(new Date());
            task.setCurrentProgress(0);
            task.setTotalProgress(totalProgress); // 说明最大值设定对当天状态不修改，管理员修改配置后需要在第二天生效
        }
        if (null == task.getCurrentProgress()) task.setCurrentProgress(0);
        task.setCurrentProgress(task.getCurrentProgress() + 1);
        return repository.save(task);
    }

    @Override
    public List<StaticTask> list(Long uid) {
        List<StaticTask> list = repository.findAllByUid(uid);
        if (list == null) list = new ArrayList<>();
        Map<StaticTask.TYPE, StaticTask> map = new HashMap();
        for (StaticTask task : list) {
            if (task == null) continue;
            map.put(task.getType(), task);
        }
        // check 4 task
        List<StaticTask> flushTasks = new ArrayList<>();
        flushTasks.add(formStaticTask(StaticTask.TYPE.xinrenlibao, map));
        return flushTasks;
    }


    private StaticTask formStaticTask(StaticTask.TYPE type, Map<StaticTask.TYPE, StaticTask> map) {
        StaticTask task = map.get(type);
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
            case xinrenlibao:
                totalProgress = taskConfigurerService.getTaskClickLikeProgress();
                break;
        }
        task = new StaticTask();
        task.setType(type);
        task.setUpdateTime(date);
        task.setTotalProgress(totalProgress);
        task.setCurrentProgress(0);
        return task;
    }

    /**
     * 领取新人礼包
     *
     * @param uid
     * @return
     */
    @Override
    public StaticTask receiveNewHerePackage(Long uid) throws NewHerePackageReceiveEmptyException {
        StaticTask task = repository.findByUidAndType(uid, StaticTask.TYPE.xinrenlibao);
        if (task == null) {
            task = new StaticTask();
            task.setUid(uid);
            task.setCurrentProgress(0);
            task.setTotalProgress(taskConfigurerService.getTaskNewHerePackageNumber());
            task.setType(StaticTask.TYPE.xinrenlibao);
            task.setUpdateTime(new Date());
        }
        if (task.getCurrentProgress() >= task.getTotalProgress()) // 表示不可以再领取了
            throw new NewHerePackageReceiveEmptyException(uid);
        //TODO many many things here (receive package to uid account)
        return update(uid, StaticTask.TYPE.xinrenlibao);
    }
}
