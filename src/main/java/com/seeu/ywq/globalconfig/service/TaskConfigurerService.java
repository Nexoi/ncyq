package com.seeu.ywq.globalconfig.service;

public interface TaskConfigurerService {
    void setTaskClickLikeProgress(Integer taskClickLikeProgress);

    Integer getTaskClickLikeProgress();

    Integer getTaskCommentProgress();

    void setTaskCommentProgress(Integer taskCommentProgress);

    Integer getTaskShareProgress();

    void setTaskShareProgress(Integer taskShareProgress);

    Integer getTaskSignInProgress();

    void setTaskSignInProgress(Integer taskSignInProgress);
}
