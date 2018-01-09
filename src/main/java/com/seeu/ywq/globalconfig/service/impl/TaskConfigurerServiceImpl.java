package com.seeu.ywq.globalconfig.service.impl;

import com.seeu.ywq.globalconfig.service.TaskConfigurerService;
import org.springframework.stereotype.Service;

@Service
public class TaskConfigurerServiceImpl implements TaskConfigurerService {

    private static final String KEY_TASK_CLICKLIKE = "task.click.like";
    private static final String KEY_TASK_COMMENT = "task.comment";
    private static final String KEY_TASK_SHARE = "task.share";
    private static final String KEY_TASK_SIGN = "task.signin";

    private Integer taskClickLikeProgress;
    private Integer taskCommentProgress;
    private Integer taskShareProgress;
    private Integer taskSignInProgress;

    @Override
    public void setTaskClickLikeProgress(Integer taskClickLikeProgress) {

    }

    @Override
    public Integer getTaskClickLikeProgress() {
        return null;
    }

    @Override
    public Integer getTaskCommentProgress() {
        return null;
    }

    @Override
    public void setTaskCommentProgress(Integer taskCommentProgress) {

    }

    @Override
    public Integer getTaskShareProgress() {
        return null;
    }

    @Override
    public void setTaskShareProgress(Integer taskShareProgress) {

    }

    @Override
    public Integer getTaskSignInProgress() {
        return null;
    }

    @Override
    public void setTaskSignInProgress(Integer taskSignInProgress) {

    }

}
