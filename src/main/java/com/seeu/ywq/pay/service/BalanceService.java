package com.seeu.ywq.pay.service;

import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.userlogin.exception.NoSuchUserException;

/**
 * 余额系统，可以查看用户余额、增加减少余额
 * <p>
 * 单位：钻石
 */
public interface BalanceService {

    Long query(Long uid) throws NoSuchUserException;

    void plus(Long uid, Long diamonds);

    void minus(Long uid, Long diamonds) throws BalanceNotEnoughException;
}
