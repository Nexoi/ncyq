package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.config.service.GlobalConfigurerService;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.Balance;
import com.seeu.ywq.pay.repository.PayBalanceRepository;
import com.seeu.ywq.pay.service.BalanceService;
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
    private GlobalConfigurerService globalConfigurerService;

    @Override
    public Long query(Long uid) throws NoSuchUserException {
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null)
            throw new NoSuchUserException("用户 [ \" + uid + \" ] 不存在");
        return balance.getBalance();
    }

    @Override
    public void plus(Long uid, Long diamonds) {
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null) {
            // 创建新用户余额系统
            balance = new Balance();
            balance.setUid(uid);
            balance.setBalance(0l);
        }
        balance.setBalance(balance.getBalance() + diamonds);
        balance.setUpdateTime(new Date());
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
            float percent = globalConfigurerService.getBindUserShareDiamondsPercent();
            plus(balance.getBindUid(), (long) (diamonds * percent));
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
            balance.setBalance(0l);
            payBalanceRepository.save(balance);
        }
    }
}
