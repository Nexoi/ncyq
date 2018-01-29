package com.seeu.ywq.pay.service;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.ywq.exception.*;
import com.seeu.ywq.gift.model.GiftOrder;
import com.seeu.ywq.gift.model.RewardOrder;
import com.seeu.ywq.globalconfig.exception.GlobalConfigSettingException;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.ExchangeTable;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.model.OrderRecharge;
import com.seeu.ywq.userlogin.exception.PhoneNumberNetSetException;
import com.seeu.ywq.userlogin.exception.WeChatNotSetException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * 各种订单的创建/查询，创建之后会自动进行后续处理（链接支付宝等）
 */
public interface OrderService {


    // 支付成功回调
    @Transactional
    void finishOrder(String orderId);

    @Transactional
    void failOrder(String orderId);

    // 充值
    @Transactional
    String createRecharge(OrderRecharge.PAY_METHOD payMethod, Long uid, BigDecimal price) throws ActionParameterException, ActionNotSupportException;

    // 购买VIP卡
    @Transactional
    String createVIPCardUseAliPay(Long uid, Long day) throws ResourceNotFoundException, ActionParameterException;

    // 购买VIP卡
    @Transactional
    String createVIPCardUseWeChat(Long uid, Long day) throws ResourceNotFoundException, ActionParameterException;


    // 报名活动
    @Transactional
    String createActivity(Long uid, Long activityId);

    /**
     * 提現
     *
     * @param uid
     * @param diamonds  钻石数量
     * @param payMethod 支付方式
     * @param payId     支付账号ID
     * @param payName   真实姓名
     * @return
     */
    @Transactional
    OrderLog createWithdraw(Long uid, Long diamonds, OrderRecharge.PAY_METHOD payMethod, String payId, String payName);

    // 购买VIP卡
    @Transactional
    OrderLog createVIPCardUseDiamond(Long uid, Long day) throws ResourceNotFoundException, BalanceNotEnoughException;

    // 将钻石转化为金币
    @Transactional
    OrderLog createTransferDiamondsToCoins(Long uid, Long diamonds) throws BalanceNotEnoughException, ActionNotSupportException;

    // 送花（直接将花转成钻石）
    @Transactional
    RewardOrder createReward(Long uid, Long herUid, Long rewardResourceId, Integer amount) throws BalanceNotEnoughException, ActionNotSupportException, ResourceNotFoundException, AmountCannotBeNegetiveException;

    @Transactional
    GiftOrder createSendGift(Long uid, Long herUid, Long resourceId, Integer amount) throws BalanceNotEnoughException, ActionNotSupportException, ResourceNotFoundException, AmountCannotBeNegetiveException;

    @Transactional
    OrderLog createUnlockPublish(Long uid, Long publishId) throws PublishNotFoundException, BalanceNotEnoughException, ResourceAlreadyActivedException, ActionNotSupportException;

    @Transactional
    OrderLog createUnlockHomePageVideo(Long uid, Long videoId) throws ResourceNotFoundException, BalanceNotEnoughException, ResourceAlreadyActivedException, ActionNotSupportException;

    @Transactional
    OrderLog createUnlockWeChatID(Long uid, Long herUid) throws BalanceNotEnoughException, WeChatNotSetException, SMSSendFailureException, GlobalConfigSettingException, ActionParameterException;

    /**
     * @param uid
     * @param herUid
     * @return phoneNumber
     * @throws BalanceNotEnoughException
     * @throws GlobalConfigSettingException
     */
    @Transactional
    String createUnlockPhoneNumber(Long uid, Long herUid) throws BalanceNotEnoughException, GlobalConfigSettingException, PhoneNumberNetSetException, ActionParameterException;

    @Transactional
    OrderLog createBindShare(Long bindUid, Long diamonds) throws ActionNotSupportException;

    Page<OrderLog> queryAll(Long uid, Pageable pageable);

    ExchangeTable queryExchange(Long uid, ExchangeTable.TYPE type, BigDecimal fromPrice) throws ActionNotSupportException;

    ExchangeTable queryExchangeReverse(Long uid, Long diamonds) throws ActionNotSupportException;

    ExchangeTable queryExchange(Long uid, ExchangeTable.TYPE type, Long diamonds) throws ActionNotSupportException;
}
