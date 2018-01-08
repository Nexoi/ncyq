package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.Balance;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.repository.OrderLogRepository;
import com.seeu.ywq.pay.repository.PayBalanceRepository;
import com.seeu.ywq.pay.service.BalanceService;
import com.seeu.ywq.pay.service.OrderLogService;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.userlogin.exception.NoSuchUserException;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

@Service
public class BalanceServiceImpl implements BalanceService {
    @Resource
    private PayBalanceRepository payBalanceRepository;
    @Autowired
    private UserReactService userReactService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLogService orderLogService;

    @Override
    public Long query(Long uid) throws NoSuchUserException {
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null)
            throw new NoSuchUserException(uid);
        return balance.getBalance();
    }

    @Override
    public Balance queryDetail(Long uid) throws NoSuchUserException {
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null)
            throw new NoSuchUserException(uid);
        balance.setBindUid(null); // 匿掉
        return balance;
    }

    @Transactional
    @Override
    public OrderLog update(String orderId, Long uid, OrderLog.EVENT event, Long diamondsDelta) throws BalanceNotEnoughException, ActionNotSupportException {
        if (diamondsDelta == null || diamondsDelta < 0)
            throw new ActionNotSupportException("交易额度必须为正整数");
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null) {
            // 创建新用户余额系统
            balance = new Balance();
            balance.setUid(uid);
            balance.setBalance(0L);
        }
        balance.setUpdateTime(new Date());
        // 更新账户
        eventUpdate(balance, event, diamondsDelta);
        payBalanceRepository.saveAndFlush(balance);
        if (event.canShareBind())
            orderService.createBindShare(balance.getBindUid(), diamondsDelta);// 上家分成
        // 消费/收入记录
        return writeLog(orderId, uid, event, diamondsDelta);
    }

    @Transactional
    @Override
    public OrderLog update(String orderId, Long uid, OrderLog.EVENT event, Long diamondsDelta, Long coinDelta) throws BalanceNotEnoughException, ActionNotSupportException {
        if (diamondsDelta == null || diamondsDelta < 0)
            throw new ActionNotSupportException("交易额度必须为正整数");
        Balance balance = payBalanceRepository.findOne(uid);
        if (balance == null) {
            // 创建新用户余额系统
            balance = new Balance();
            balance.setUid(uid);
            balance.setBalance(0L);
        }
        balance.setUpdateTime(new Date());
        // 更新账户
        eventUpdate(balance, event, diamondsDelta, coinDelta);
        payBalanceRepository.saveAndFlush(balance);
        // 消费/收入记录
        return writeLog(orderId, uid, event, diamondsDelta);
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
            balance.setRewardExpense(0L);
            balance.setWithdraw(0L);
            balance.setRecharge(0L);
            balance.setCoin(0L);
            payBalanceRepository.save(balance);
        }
    }

    // 相对于自己，主语：I
    private void eventUpdate(Balance balance, OrderLog.EVENT event, Long diamondsDelta) throws BalanceNotEnoughException {
        if (balance == null) return;
        Long diamonds = 0L;
        switch (event) {
            case RECHARGE:
                diamonds = balance.getRecharge();
                if (diamonds == null) diamonds = 0L;
                balance.setRecharge(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() + diamondsDelta); // 充值额度需要加入余额
                break;
            case WITHDRAW:
                checkBalance(balance.getBalance(), diamondsDelta);  // check
                diamonds = balance.getWithdraw();
                if (diamonds == null) diamonds = 0L;
                balance.setWithdraw(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() - diamondsDelta); // 提现额度需要减少余额
                break;
            case REWARD_EXPENSE:
                diamonds = balance.getRewardExpense();
                if (diamonds == null) diamonds = 0L;
                balance.setRewardExpense(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() - diamondsDelta); // 打赏别人
                break;
            case REWARD_RECEIVE:
                diamonds = balance.getRewardReceive();
                if (diamonds == null) diamonds = 0L;
                balance.setRewardReceive(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() + diamondsDelta); // 收到打赏
                break;
            case UNLOCK_PUBLISH:
                checkBalance(balance.getBalance(), diamondsDelta);  // check
                diamonds = balance.getPublishExpense();
                if (diamonds == null) diamonds = 0L;
                balance.setPublishExpense(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() - diamondsDelta); // 解锁
                break;
            case RECEIVE_PUBLISH:
                diamonds = balance.getPublishReceive();
                if (diamonds == null) diamonds = 0L;
                balance.setPublishReceive(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() + diamondsDelta); // 收到解锁金额
                break;
            case UNLOCK_WECHAT:
                checkBalance(balance.getBalance(), diamondsDelta);  // check
                diamonds = balance.getWechatExpense();
                if (diamonds == null) diamonds = 0L;
                balance.setWechatExpense(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() - diamondsDelta); // 支出
            case RECEIVE_WECHAT:
                diamonds = balance.getWechatReceive();
                if (diamonds == null) diamonds = 0L;
                balance.setWechatReceive(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() + diamondsDelta); // 收获
                break;
            case BIND_SHARED_EXPENSE:
                diamonds = balance.getSharedExpense();
                if (diamonds == null) diamonds = 0L;
                balance.setSharedExpense(diamonds + diamondsDelta);
//                balance.setBalance(balance.getBalance() - diamondsDelta);  // 支出，不用再支出，系统已经提前自动计账，余额不变
            case BIND_SHARED_RECEIVE:
                diamonds = balance.getSharedReceive();
                if (diamonds == null) diamonds = 0L;
                balance.setSharedReceive(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() + diamondsDelta); // 作为上家分成
                break;
            case UNLOCK_PHONE:
                checkBalance(balance.getBalance(), diamondsDelta);  // check
                diamonds = balance.getPhoneExpense();
                if (diamonds == null) diamonds = 0L;
                balance.setPhoneExpense(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() - diamondsDelta); // 支出
                break;
            case RECEIVE_PHONE:
                diamonds = balance.getPhoneReceive();
                if (diamonds == null) diamonds = 0L;
                balance.setPhoneReceive(diamonds + diamondsDelta);
                balance.setBalance(balance.getBalance() + diamondsDelta); // 收获
                break;
            case DIAMOND_TO_COIN:

            default:
                break;
        }
    }

    private void eventUpdate(Balance balance, OrderLog.EVENT event, Long diamondsDelta, Long coinDelta) throws BalanceNotEnoughException {
        if (event == null || event != OrderLog.EVENT.DIAMOND_TO_COIN) return;
        if (balance == null) return;
        checkBalance(balance.getBalance(), diamondsDelta);  // check
        Long coin = balance.getCoin();
        if (coin == null) coin = 0L;
        balance.setCoin(coin + coinDelta);
        balance.setBalance(balance.getBalance() - diamondsDelta);
    }

    private void checkBalance(Long balance, Long expense) throws BalanceNotEnoughException {
        if (balance == null || expense == null) return;
        if (balance < expense) throw new BalanceNotEnoughException("账户余额不足");
    }

    private OrderLog writeLog(String orderId, Long uid, OrderLog.EVENT event, Long diamonds) {
        OrderLog log = new OrderLog();
        log.setOrderId(orderId);
        log.setCreateTime(new Date());
        log.setUid(uid);
        log.setEvent(event);
        log.setType(event.isExpense() ? OrderLog.TYPE.OUT : OrderLog.TYPE.IN);
        log.setDiamonds(diamonds);
        return orderLogService.save(log);
    }
}
