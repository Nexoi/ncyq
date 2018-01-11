package com.seeu.ywq.pay.service.impl;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.sms.SMSService;
import com.seeu.ywq.event_listener.order_event.ReceiveRewardEvent;
import com.seeu.ywq.exception.*;
import com.seeu.ywq.gift.model.GiftOrder;
import com.seeu.ywq.gift.model.Reward;
import com.seeu.ywq.gift.service.GiftOrderService;
import com.seeu.ywq.gift.service.RewardService;
import com.seeu.ywq.globalconfig.exception.GlobalConfigSettingException;
import com.seeu.ywq.globalconfig.service.GlobalConfigurerService;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.ExchangeTable;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.model.OrderRecharge;
import com.seeu.ywq.pay.repository.OrderLogRepository;
import com.seeu.ywq.pay.service.BalanceService;
import com.seeu.ywq.pay.service.ExchangeTableService;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.resource.service.ResourceAuthService;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.service.PublishService;
import com.seeu.ywq.userlogin.exception.WeChatNotSetException;
import com.seeu.ywq.userlogin.service.UserReactService;
import com.seeu.ywq.uservip.model.UserVIP;
import com.seeu.ywq.uservip.model.VIPTable;
import com.seeu.ywq.uservip.service.UserVIPService;
import com.seeu.ywq.uservip.service.VIPTableService;
import com.seeu.ywq.utils.DateFormatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private BalanceService balanceService;
    @Autowired
    private PublishService publishService;
    @Autowired
    private ResourceAuthService resourceAuthService;
    @Autowired
    private UserReactService userReactService;
    @Autowired
    private SMSService smsService;
    @Resource
    private OrderLogRepository orderLogRepository;
    @Autowired
    private GlobalConfigurerService globalConfigurerService;
    @Autowired
    private ExchangeTableService exchangeTableService;
    @Autowired
    private ApplicationContext applicationContext;

    @Value("${ywq.resource.interval.publish}")
    private Integer timeInterval_Publish;

    @Value("${ywq.sms.unlock_wechat}")
    private String unlockWeChatSMSText;

    // 打赏
    @Autowired
    private RewardService rewardService;
    @Autowired
    private GiftOrderService giftOrderService;
    // VIP
    @Autowired
    private VIPTableService vipTableService;
    @Autowired
    private UserVIPService userVIPService;

    @Override
    public OrderLog createRecharge(OrderRecharge.PAY_METHOD payMethod, Long uid, Long diamonds) {
        return null;
    }

    @Override
    public OrderLog createWithdraw(Long uid, Long diamonds, OrderRecharge.PAY_METHOD payMethod, String payId, String payName) {
        return null;
    }

    @Override
    public OrderLog createVIPCardUseDiamond(Long uid, Long day) throws ResourceNotFoundException, BalanceNotEnoughException {
        // 查找资源
        VIPTable vipTable = vipTableService.findByDay(day);
        if (vipTable == null)
            throw new ResourceNotFoundException("Can not found Resource [VIP: " + day + " day ]");
        // 本地支付
        Long diamonds = vipTable.getDiamonds();
        try {
            OrderLog log = balanceService.update(genOrderID(), uid, OrderLog.EVENT.BUY_VIP, diamonds);
            // 激活会员
            Date date = new Date();
            UserVIP vip = userVIPService.findOne(uid);
            if (vip == null) {
                vip.setVipLevel(UserVIP.VIP.none);
                vip.setTerminationDate(date);
                vip.setUid(uid);
            }
            if (vip.getTerminationDate() == null || vip.getTerminationDate().before(date))// 在今天之前（表示过期了）
                vip.setTerminationDate(date);
            vip.setUpdateTime(date);
            vip.setVipLevel(UserVIP.VIP.vip);
            long time = vipTable.getDay() * 24 * 60 * 60 * 1000;
            vip.setTerminationDate(new Date(vip.getTerminationDate().getTime() + time));
            userVIPService.save(vip);
            return log;
        } catch (ActionNotSupportException e) {
            e.printStackTrace();// 不可能事件
            return null;
        }
    }

    @Override
    public void createVIPCardUseAliPay(Long uid, Long day) {

    }

    @Override
    public void createVIPCardUseWeChat(Long uid, Long day) {

    }

    // 钻石换金币
    @Override
    public OrderLog createTransferDiamondsToCoins(Long uid, Long diamonds) throws BalanceNotEnoughException, ActionNotSupportException {
        ExchangeTable exchangeTable = queryExchange(uid, ExchangeTable.TYPE.DIAMOND2COIN, diamonds);
        if (exchangeTable == null || exchangeTable.getTo() == null || exchangeTable.getTo().compareTo(BigDecimal.ZERO) == 0)
            throw new ActionNotSupportException("操作不被允许，兑换的额度不能为 0 ");
        return balanceService.update(genOrderID(), uid, OrderLog.EVENT.DIAMOND_TO_COIN, diamonds, exchangeTable.getTo().longValue());
    }


    /**
     * 送花（打赏物品）
     *
     * @param uid
     * @param herUid
     * @param rewardResourceId
     * @param amount
     * @return
     * @throws BalanceNotEnoughException
     * @throws RewardResourceNotFoundException
     * @throws RewardAmountCannotBeNegitiveException
     * @throws ActionNotSupportException             资源设定异常，价格不对（负数？）
     */
    @Transactional
    @Override
    public GiftOrder createReward(Long uid, Long herUid, Long rewardResourceId, Integer amount) throws BalanceNotEnoughException, RewardResourceNotFoundException, RewardAmountCannotBeNegitiveException, ActionNotSupportException {
        // 判断资源是否存在
        Reward reward = rewardService.findOne(rewardResourceId);
        if (reward == null) throw new RewardResourceNotFoundException();
        // 计算总价
        if (amount <= 0) throw new RewardAmountCannotBeNegitiveException();
        Long price = reward.getDiamonds() * amount;
        String orderID = genOrderID();
        // 观看者用户扣钱
        balanceService.update(orderID, uid, OrderLog.EVENT.REWARD_EXPENSE, price);
        // 收钱
        // 发布者用户收钱 （百分比配）
        Long transactionPrice = (long) (price * globalConfigurerService.getUserDiamondsPercent());
        balanceService.update(orderID, herUid, OrderLog.EVENT.REWARD_RECEIVE, transactionPrice);
        // 记录订单（送礼单独的订单）
        GiftOrder giftOrder = new GiftOrder();
        giftOrder.setOrderId(orderID);
        giftOrder.setCreateTime(new Date());
        giftOrder.setDiamonds(price);
        giftOrder.setUid(uid);
        giftOrder.setHerUid(herUid);
        giftOrder.setRewardResourceId(rewardResourceId);
        giftOrder = giftOrderService.save(giftOrder);
        // 通知
        applicationContext.publishEvent(new ReceiveRewardEvent(this, herUid, uid, "", rewardResourceId, reward.getName(), amount, price, transactionPrice, orderID));
        return giftOrder;
    }

    @Override
    public OrderLog createSendGift() {
        return null;
    }

    // 解锁动态
    @Transactional
    @Override
    public OrderLog createUnlockPublish(Long uid, Long publishId) throws PublishNotFoundException, BalanceNotEnoughException, ResourceAlreadyActivedException, ActionNotSupportException {
        // 判断资源
        Publish publish = publishService.findOne(publishId);
        if (publish == null)
            throw new PublishNotFoundException("动态不存在！");
        Long herUid = publish.getUid();
        // 查看是否在激活状态
        if (resourceAuthService.canVisit(uid, publishId))
            throw new ResourceAlreadyActivedException();
        Long diamonds = publish.getUnlockPrice().longValue();
        String orderID = genOrderID();
        // 观看者用户扣钱
        OrderLog log = balanceService.update(orderID, uid, OrderLog.EVENT.UNLOCK_PUBLISH, diamonds);
        // 发布者用户收钱 （百分比配）
        diamonds = (long) (diamonds * globalConfigurerService.getUserDiamondsPercent());
        balanceService.update(orderID, herUid, OrderLog.EVENT.RECEIVE_PUBLISH, diamonds);
        // 激活权限
        resourceAuthService.activeResource(uid, publishId, timeInterval_Publish); // 默认一天
        return log;
    }

    @Override
    public OrderLog createUnlockWeChatID(Long uid, Long herUid) throws BalanceNotEnoughException, WeChatNotSetException, SMSSendFailureException, GlobalConfigSettingException {
        // 查询微信号
        String wechat = userReactService.getWeChatID(herUid);
        if (wechat == null)
            throw new WeChatNotSetException();
        // 查看自己的手机号码
        String myPhone = userReactService.getPhone(uid);
        if (myPhone == null)
            throw new SMSSendFailureException(myPhone, "手机号码为空，发送失败");

        // 记录账单
        Long diamonds = globalConfigurerService.getUnlockWeChat();
        String orderId = genOrderID();
        OrderLog log = null;
        try {
            // 观看者用户扣钱
            log = balanceService.update(orderId, uid, OrderLog.EVENT.UNLOCK_WECHAT, diamonds);
        } catch (ActionNotSupportException e) {
            throw new GlobalConfigSettingException("系统设置异常！微信解锁金额设定错误");
        }
        // 发送短信
        smsService.send(myPhone, ("" + unlockWeChatSMSText).replace("%wechat%", wechat));

        // 发布者用户收钱 （百分比配）
        diamonds = (long) (diamonds * globalConfigurerService.getUserDiamondsPercent());
        try {
            balanceService.update(orderId, herUid, OrderLog.EVENT.UNLOCK_WECHAT, diamonds);
        } catch (ActionNotSupportException e) {
            e.printStackTrace(); // 不可能的
        }
        return log;
    }

    @Override
    public OrderLog createBindShare(Long bindUid, Long diamonds) throws ActionNotSupportException {
        if (diamonds < 0)
            throw new ActionNotSupportException("分成额度不能为负数");
        try {
            diamonds = (long) (diamonds * globalConfigurerService.getBindUserShareDiamondsPercent());
            // 加钱
            return balanceService.update(genOrderID(), bindUid, OrderLog.EVENT.BIND_SHARED_RECEIVE, diamonds);
        } catch (BalanceNotEnoughException e) { // 不可能的！
            e.printStackTrace();
            return null;
        } catch (ActionNotSupportException e) {
            e.printStackTrace();
            throw new ActionNotSupportException("分成额度不能为负数");
        }
    }

    @Override
    public Page<OrderLog> queryAll(Long uid, Pageable pageable) {
        return orderLogRepository.findAllByUid(uid, pageable);
    }

    @Override
    public ExchangeTable queryExchange(Long uid, ExchangeTable.TYPE type, BigDecimal fromPrice) throws ActionNotSupportException {
        if (type == null || fromPrice == null) return null;
        ExchangeTable exchangeTable = exchangeTableService.findByTypeAndFromPrice(type, fromPrice);

        if (exchangeTable != null) {
            exchangeTable.setId(null);
            exchangeTable.setUpdateTime(null);
            exchangeTable.setType(null);
            return exchangeTable;
        }
        if (type == ExchangeTable.TYPE.RMB2DIAMOND) {
            // 如果查找不到配置表，则按比例汇算
            exchangeTable = new ExchangeTable();
            exchangeTable.setFrom(fromPrice);
            exchangeTable.setTo(BigDecimal.valueOf(globalConfigurerService.getDiamondsRatioToRMB(fromPrice)));
            return exchangeTable;
        }
        if (type == ExchangeTable.TYPE.DIAMOND2COIN) {
            BigDecimal fromPriceLong = BigDecimal.valueOf(fromPrice.longValue());
            if (fromPriceLong.compareTo(fromPrice) != 0)
                throw new ActionNotSupportException("传入钻石数必须为整数");
            // 如果查找不到配置表，则按比例汇算
            exchangeTable = new ExchangeTable();
            exchangeTable.setFrom(fromPrice);
            exchangeTable.setTo(BigDecimal.valueOf(globalConfigurerService.getCoinsRatioToDiamonds(fromPrice.longValue())));
            return exchangeTable;
        }
        return null;
    }

    // 钻石换金币
    @Override
    public ExchangeTable queryExchangeReverse(Long uid, Long diamonds) throws ActionNotSupportException {
        if (diamonds == null || diamonds <= 0)
            throw new ActionNotSupportException("传入钻石数必须为正整数");
        ExchangeTable exchangeTable = exchangeTableService.findByTypeAndToDiamonds(ExchangeTable.TYPE.RMB2DIAMOND, diamonds);
        if (exchangeTable != null) {
            exchangeTable.setId(null);
            exchangeTable.setType(null);
            exchangeTable.setUpdateTime(null);
            return exchangeTable;
        }
        // 如果查找不到配置表，则按比例汇算
        exchangeTable = new ExchangeTable();
        exchangeTable.setFrom(globalConfigurerService.getRMBRatioToDiamonds(diamonds));
        exchangeTable.setTo(BigDecimal.valueOf(diamonds));
        return exchangeTable;
    }

    @Override
    public ExchangeTable queryExchange(Long uid, ExchangeTable.TYPE type, Long fromPrice) throws ActionNotSupportException {
        return queryExchange(uid, type, BigDecimal.valueOf(fromPrice));
    }


    private String genOrderID() {
        SimpleDateFormat format = dateFormatterService.getyyyyMMddHHmmssS();
        String dateStr = format.format(new Date());
        return dateStr + (new Random().nextInt(900) + 100);
    }

    @Autowired
    private DateFormatterService dateFormatterService;
}
