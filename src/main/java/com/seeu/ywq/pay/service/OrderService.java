package com.seeu.ywq.pay.service;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.ywq.exception.RewardAmountCannotBeNegitiveException;
import com.seeu.ywq.exception.RewardResourceNotFoundException;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.model.OrderRecharge;
import com.seeu.ywq.exception.PublishNotFoundException;
import com.seeu.ywq.exception.ResourceAlreadyActivedException;
import com.seeu.ywq.userlogin.exception.WeChatNotSetException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 各种订单的创建/查询，创建之后会自动进行后续处理（链接支付宝等）
 */
public interface OrderService {

    // 充值
    OrderLog createRecharge(OrderRecharge.PAY_METHOD payMethod, Long uid, Long diamonds);

    /**
     * @param uid
     * @param diamonds  钻石数量
     * @param payMethod 支付方式
     * @param payId     支付账号ID
     * @param payName   真实姓名
     * @return
     */
    OrderLog createWithdraw(Long uid, Long diamonds, OrderRecharge.PAY_METHOD payMethod, String payId, String payName);

    // 送花（直接将花转成钻石）
    OrderLog createReward(Long uid, Long herUid, Long rewardResourceId, Integer amount) throws BalanceNotEnoughException, RewardResourceNotFoundException, RewardAmountCannotBeNegitiveException;

    OrderLog createSendGift();

    OrderLog createUnlockPublish(Long uid, Long publishId) throws PublishNotFoundException, BalanceNotEnoughException, ResourceAlreadyActivedException;

    OrderLog createUnlockWeChatID(Long uid, Long herUid) throws BalanceNotEnoughException, WeChatNotSetException, SMSSendFailureException;

    OrderLog createBindShare(Long bindUid, Long diamonds);

    Page<OrderLog> queryAll(Long uid, Pageable pageable);
}
