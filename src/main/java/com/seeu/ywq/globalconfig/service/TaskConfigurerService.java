package com.seeu.ywq.globalconfig.service;

import com.seeu.ywq.exception.ActionNotSupportException;

public interface TaskConfigurerService {

    Integer getTaskClickLikeProgress();

    void setTaskClickLikeProgress(Integer taskClickLikeProgress) throws ActionNotSupportException;

    Integer getTaskCommentProgress();

    void setTaskCommentProgress(Integer taskCommentProgress) throws ActionNotSupportException;

    Integer getTaskShareProgress();

    void setTaskShareProgress(Integer taskShareProgress) throws ActionNotSupportException;

    Integer getTaskSignInProgress();

    void setTaskSignInProgress(Integer taskSignInProgress) throws ActionNotSupportException;


    // 新人礼包数量
    void setTaskNewHerePackageNumber(Integer number) throws ActionNotSupportException;

    Integer getTaskNewHerePackageNumber();
}
