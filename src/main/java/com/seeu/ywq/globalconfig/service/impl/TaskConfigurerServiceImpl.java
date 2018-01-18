package com.seeu.ywq.globalconfig.service.impl;

import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.globalconfig.model.GlobalConfigurer;
import com.seeu.ywq.globalconfig.repository.GlobalConfigurerRepository;
import com.seeu.ywq.globalconfig.service.TaskConfigurerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaskConfigurerServiceImpl implements TaskConfigurerService {

    private static final String KEY_TASK_CLICKLIKE = "task.click_like";
    private static final String KEY_TASK_COMMENT = "task.comment";
    private static final String KEY_TASK_SHARE = "task.share";
    private static final String KEY_TASK_SIGN = "task.signin";
    private static final String KEY_TASK_NEWHERE_PACKAGE_NUMBER = "task.new_here_package_number";

    private Integer taskClickLikeProgress;
    private Integer taskCommentProgress;
    private Integer taskShareProgress;
    private Integer taskSignInProgress;
    private Integer taskNewHerePackageNumber;

    @Resource
    private GlobalConfigurerRepository repository;

    private String findOne(String attrName) {
        GlobalConfigurer configurer = repository.findOne(attrName);
        return (configurer == null) ? null : configurer.getAttrValue();
    }

    private void setValue(String key, String value) {
        GlobalConfigurer configurer = repository.findOne(key);
        if (configurer == null) {
            configurer = new GlobalConfigurer();
            configurer.setAttrKey(key);
        }
        configurer.setAttrValue(String.valueOf(value));
        repository.save(configurer);
    }

    @Override
    public void setTaskClickLikeProgress(Integer taskClickLikeProgress) throws ActionNotSupportException {
        if (taskClickLikeProgress < 0)
            throw new ActionNotSupportException("设定值不能为负数");
        setValue(KEY_TASK_CLICKLIKE, String.valueOf(taskClickLikeProgress));
        this.taskClickLikeProgress = taskClickLikeProgress;
    }

    @Override
    public Integer getTaskClickLikeProgress() {
        if (taskClickLikeProgress != null)
            return taskClickLikeProgress;
        String times = findOne(KEY_TASK_CLICKLIKE);
        if (times == null) {
            // reset by a default suitable value
            try {
                setTaskClickLikeProgress(5);
                times = findOne(KEY_TASK_CLICKLIKE);
            } catch (ActionNotSupportException e) {
                e.printStackTrace();
            }
        }
        taskClickLikeProgress = times == null ? 0 : Integer.parseInt(times);
        return taskClickLikeProgress;
    }

    @Override
    public Integer getTaskCommentProgress() {
        if (taskCommentProgress != null)
            return taskCommentProgress;
        String times = findOne(KEY_TASK_COMMENT);
        if (times == null) {
            // reset by a default suitable value
            try {
                setTaskCommentProgress(5);
                times = findOne(KEY_TASK_COMMENT);
            } catch (ActionNotSupportException e) {
                e.printStackTrace();
            }
        }
        taskCommentProgress = times == null ? 0 : Integer.parseInt(times);
        return taskCommentProgress;
    }

    @Override
    public void setTaskCommentProgress(Integer taskCommentProgress) throws ActionNotSupportException {
        if (taskCommentProgress < 0)
            throw new ActionNotSupportException("设定值不能为负数");
        setValue(KEY_TASK_COMMENT, String.valueOf(taskCommentProgress));
        this.taskCommentProgress = taskCommentProgress;
    }

    @Override
    public Integer getTaskShareProgress() {
        if (taskShareProgress != null)
            return taskShareProgress;
        String times = findOne(KEY_TASK_SHARE);
        if (times == null) {
            // reset by a default suitable value
            try {
                setTaskShareProgress(5);
                times = findOne(KEY_TASK_SHARE);
            } catch (ActionNotSupportException e) {
                e.printStackTrace();
            }
        }
        taskShareProgress = times == null ? 0 : Integer.parseInt(times);
        return taskShareProgress;
    }

    @Override
    public void setTaskShareProgress(Integer taskShareProgress) throws ActionNotSupportException {
        if (taskShareProgress < 0)
            throw new ActionNotSupportException("设定值不能为负数");
        setValue(KEY_TASK_SHARE, String.valueOf(taskShareProgress));
        this.taskShareProgress = taskShareProgress;
    }

    // always 1
    @Override
    public Integer getTaskSignInProgress() {
        return 1;
    }

    @Override
    public void setTaskSignInProgress(Integer taskSignInProgress) {
        // do nothing
    }

    @Override
    public void setTaskNewHerePackageNumber(Integer taskNewHerePackageNumber) throws ActionNotSupportException {
        if (taskNewHerePackageNumber < 0)
            throw new ActionNotSupportException("设定值不能为负数");
        setValue(KEY_TASK_NEWHERE_PACKAGE_NUMBER, String.valueOf(taskNewHerePackageNumber));
        this.taskNewHerePackageNumber = taskNewHerePackageNumber;
    }

    @Override
    public Integer getTaskNewHerePackageNumber() {
        if (taskNewHerePackageNumber != null)
            return taskNewHerePackageNumber;
        String packages = findOne(KEY_TASK_NEWHERE_PACKAGE_NUMBER);
        if (packages == null) {
            // reset by a default suitable value
            try {
                setTaskNewHerePackageNumber(1);
                packages = findOne(KEY_TASK_NEWHERE_PACKAGE_NUMBER);
            } catch (ActionNotSupportException e) {
                e.printStackTrace();
            }
        }
        taskNewHerePackageNumber = packages == null ? 0 : Integer.parseInt(packages);
        return taskNewHerePackageNumber;
    }

}
