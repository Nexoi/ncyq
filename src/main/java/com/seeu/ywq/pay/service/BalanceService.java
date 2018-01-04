package com.seeu.ywq.pay.service;

import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.Balance;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.userlogin.exception.NoSuchUserException;

/**
 * 余额系统，可以查看用户余额、增加减少余额
 * <p>
 * 单位：钻石
 */
public interface BalanceService {

    Long query(Long uid) throws NoSuchUserException;

    Balance queryDetail(Long uid) throws NoSuchUserException;

    void plus(Long uid, Long diamonds, OrderLog.EVENT event);

    void minus(Long uid, Long diamonds) throws BalanceNotEnoughException;

    // 初始化一个账户，如果该账户存在，则不初始化
    void initAccount(Long uid, Long bindUid);
}
