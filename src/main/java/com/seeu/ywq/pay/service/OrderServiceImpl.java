package com.seeu.ywq.pay.service;

import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.Balance;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.model.OrderRecharge;
import com.seeu.ywq.release.exception.PublishNotFoundException;
import com.seeu.ywq.release.model.Publish;
import com.seeu.ywq.release.repository.OrderLogRepository;
import com.seeu.ywq.release.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private BalanceService balanceService;
    @Autowired
    private PublishService publishService;
    @Resource
    private OrderLogRepository orderLogRepository;

    @Override
    public OrderLog createRecharge(OrderRecharge.PAY_METHOD payMethod, Long uid, Long diamonds) {
        return null;
    }

    @Override
    public OrderLog createWithdraw() {
        return null;
    }

    @Override
    public OrderLog createReward() {
        return null;
    }

    @Override
    public OrderLog createSendGift() {
        return null;
    }

    @Transactional
    @Override
    public OrderLog createUnlockPublish(Long uid, Long publishId) throws PublishNotFoundException, BalanceNotEnoughException {
        // 判断资源
        Publish publish = publishService.findOne(publishId);
        if (publish == null)
            throw new PublishNotFoundException("动态不存在！");

        Long diamonds = publish.getUnlockPrice().longValue();
        // 扣钱
        balanceService.minus(uid, diamonds);
        // 记录订单
        OrderLog log = new OrderLog();
        log.setOrderId(genOrderID());
        log.setCreateTime(new Date());
        log.setDiamonds(diamonds);
        log.setEvent(OrderLog.EVENT.UNLOCK_PUBLISH);
        log.setType(OrderLog.TYPE.OUT);
        log.setUid(uid);
        return orderLogRepository.save(log);
    }

    @Override
    public OrderLog createUnlockWeChatID(Long uid, Long herUid) throws BalanceNotEnoughException {
        return null;
    }

    private String genOrderID() {
        return "";
    }
}
