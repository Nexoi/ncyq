package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.config.service.GlobalConfigurerService;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.Balance;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.repository.PayBalanceRepository;
import com.seeu.ywq.pay.service.BalanceService;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.userlogin.exception.NoSuchUserException;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class BalanceServiceImpl implements BalanceService {
    @Resource
    private PayBalanceRepository payBalanceRepository;
    @Autowired
    private UserReactService userReactService;
    @Autowired
    private OrderService orderService;

    @Override
    public Long query(Long uid) throws NoSuchUserException {
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null)
            throw new NoSuchUserException("用户 [ \" + uid + \" ] 不存在");
        return balance.getBalance();
    }

    @Override
    public void plus(Long uid, Long diamonds, OrderLog.EVENT event) {
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null) {
            // 创建新用户余额系统
            balance = new Balance();
            balance.setUid(uid);
            balance.setBalance(0l);
        }
        balance.setBalance(balance.getBalance() + diamonds);
        balance.setUpdateTime(new Date());
        // TODO 对应事件记录 收入则记录，支出不记录
        eventUpdate(balance, event, diamonds);
        payBalanceRepository.saveAndFlush(balance);
    }

    @Override
    public void minus(Long uid, Long diamonds) throws BalanceNotEnoughException {
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null) {
            // 创建新用户余额系统
            balance = new Balance();
            balance.setUid(uid);
            balance.setBalance(0l);
        }
        if (balance.getBalance() < diamonds)
            throw new BalanceNotEnoughException("用户 [ " + uid + " ] 余额不足");
        balance.setBalance(balance.getBalance() - diamonds);
        balance.setUpdateTime(new Date());
        payBalanceRepository.saveAndFlush(balance);
        // 绑定者获利 10%? 份额
        if (balance.getBindUid() != null) {
            orderService.createBindShare(balance.getBindUid(), diamonds);// 该方法会自动计算比例，记录日志
//            float percent = globalConfigurerService.getBindUserShareDiamondsPercent();
//            plus(balance.getBindUid(), (long) (diamonds * percent), OrderLog.EVENT.BIND_SHARE); // TODO~
            // 记录日志

        }
    }

    @Override
    public void initAccount(Long uid, Long bindUid) {
        // 查看是否有这个绑定账户
        if (bindUid == null || !userReactService.exists(bindUid))
            bindUid = null;
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null) {
            // 创建新用户余额系统
            balance = new Balance();
            balance.setUid(uid);
            balance.setBindUid(bindUid);
            balance.setBalance(0L);
            balance.setSharedReceive(0L);
            balance.setWechatReceive(0L);
            balance.setPublishReceive(0L);
            balance.setRewardReceive(0L);
            balance.setReward(0L);
            balance.setWithdraw(0L);
            balance.setRecharge(0L);
            payBalanceRepository.save(balance);
        }
    }

    private void eventUpdate(Balance balance, OrderLog.EVENT event, Long diamondsDelta) {
        if (balance == null) return;
        Long diamonds = 0L;
        switch (event) {
            case RECHARGE:
                diamonds = balance.getRecharge();
                if (diamonds == null) diamonds = 0L;
                balance.setRecharge(diamonds + diamondsDelta);
                break;
            case WITHDRAW:
                diamonds = balance.getWithdraw();
                if (diamonds == null) diamonds = 0L;
                balance.setWithdraw(diamonds + diamondsDelta);
                break;
            case REWARD:
                diamonds = balance.getReward();
                if (diamonds == null) diamonds = 0L;
                balance.setReward(diamonds + diamondsDelta);
                break;
            case RECEIVE_REWARD:
                diamonds = balance.getRewardReceive();
                if (diamonds == null) diamonds = 0L;
                balance.setRewardReceive(diamonds + diamondsDelta);
                break;
            case UNLOCK_PUBLISH:
                diamonds = balance.getPublishReceive();
                if (diamonds == null) diamonds = 0L;
                balance.setPublishReceive(diamonds + diamondsDelta);
                break;
            case UNLOCK_WECHAT:
                diamonds = balance.getWechatReceive();
                if (diamonds == null) diamonds = 0L;
                balance.setWechatReceive(diamonds + diamondsDelta);
                break;
            case BIND_SHARE:
                diamonds = balance.getSharedReceive();
                if (diamonds == null) diamonds = 0L;
                balance.setSharedReceive(diamonds + diamondsDelta);
                break;
            default:
                break;
        }
    }
}
