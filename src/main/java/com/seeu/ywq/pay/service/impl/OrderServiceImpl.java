package com.seeu.ywq.pay.service.impl;

import com.seeu.ywq.utils.Util4IP;
import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.payment.alipay.AliPayService;
import com.seeu.third.payment.wxpay.WxPayService;
import com.seeu.third.sms.SMSService;
import com.seeu.ywq.event_listener.order_event.ReceiveGiftEvent;
import com.seeu.ywq.event_listener.order_event.ReceiveRewardEvent;
import com.seeu.ywq.exception.*;
import com.seeu.ywq.gift.model.Gift;
import com.seeu.ywq.gift.model.GiftOrder;
import com.seeu.ywq.gift.model.Reward;
import com.seeu.ywq.gift.model.RewardOrder;
import com.seeu.ywq.gift.service.GiftOrderService;
import com.seeu.ywq.gift.service.GiftService;
import com.seeu.ywq.gift.service.RewardOrderService;
import com.seeu.ywq.gift.service.RewardService;
import com.seeu.ywq.globalconfig.exception.GlobalConfigSettingException;
import com.seeu.ywq.globalconfig.service.GlobalConfigurerService;
import com.seeu.ywq.page_video.model.HomePageVideo;
import com.seeu.ywq.page_video.service.HomePageVideoService;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.ExchangeTable;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.model.OrderRecharge;
import com.seeu.ywq.pay.model.TradeModel;
import com.seeu.ywq.pay.repository.OrderLogRepository;
import com.seeu.ywq.pay.service.BalanceService;
import com.seeu.ywq.pay.service.ExchangeTableService;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.pay.service.TradeService;
import com.seeu.ywq.resource.service.ResourceAuthService;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.service.PublishService;
import com.seeu.ywq.userlogin.exception.PhoneNumberNetSetException;
import com.seeu.ywq.userlogin.exception.WeChatNotSetException;
import com.seeu.ywq.userlogin.service.UserReactService;
import com.seeu.ywq.uservip.model.UserVIP;
import com.seeu.ywq.uservip.model.VIPTable;
import com.seeu.ywq.uservip.service.UserVIPService;
import com.seeu.ywq.uservip.service.VIPTableService;
import com.seeu.ywq.utils.DateFormatterService;
import com.seeu.ywq.ywqactivity.model.Activity;
import com.seeu.ywq.ywqactivity.model.ActivityCheckIn;
import com.seeu.ywq.ywqactivity.service.ActivityCheckInService;
import com.seeu.ywq.ywqactivity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
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
    private HomePageVideoService homePageVideoService;
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
    private RewardOrderService rewardOrderService;
    // gift
    @Autowired
    private GiftService giftService;
    @Autowired
    private GiftOrderService giftOrderService;

    // VIP
    @Autowired
    private VIPTableService vipTableService;
    @Autowired
    private UserVIPService userVIPService;

    // Pay
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private Util4IP util4IP;

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityCheckInService activityCheckInService;

    @Override
    public void finishOrder(String orderId) {

    }

    @Override
    public void failOrder(String orderId) {

    }

    @Override
    public String createRecharge(OrderRecharge.PAY_METHOD payMethod, Long uid, BigDecimal price, HttpServletRequest request) throws ActionParameterException, ActionNotSupportException {
        if (payMethod == null) throw new ActionParameterException("payMethod");
        // 查询价格
        ExchangeTable table = queryExchange(null, ExchangeTable.TYPE.RMB2DIAMOND, price);
        if (table == null || table.getTo() == null) throw new ActionNotSupportException("无此配置表可选择充值");
        Long diamonds = table.getTo().longValue();
        String subject = "尤物圈钻石充值【 " + diamonds + " 个】";
        String body = subject;
        String ip = util4IP.getIpAddress(request);
        String deviceInfo = request.getHeader("User-Agent");
        if (payMethod == OrderRecharge.PAY_METHOD.ALIPAY)
            return placeOrder(genOrderID(), TradeModel.TYPE.RECHARGE, TradeModel.PAYMENT.ALIPAY, uid, price, subject, body, ip, deviceInfo, "" + diamonds);
        else
            return placeOrder(genOrderID(), TradeModel.TYPE.RECHARGE, TradeModel.PAYMENT.WECHAT, uid, price, subject, body, ip, deviceInfo, "" + diamonds);

    }

//    @Override
//    public void finishRecharge(String orderId) throws ResourceNotFoundException {
//        try {
//            TradeModel trade = tradeService.findOne(orderId);
//            if (trade == null) return;
//            tradeService.updateStatus(orderId, TradeModel.TRADE_STATUS.TRADE_FINISHED); // 完成，一律不允许退货
//            balanceService.update(orderId, trade.getUid(), OrderLog.EVENT.RECHARGE, trade.getDiamonds());
//        } catch (BalanceNotEnoughException e) {
//            e.printStackTrace();
//        } catch (ActionNotSupportException e) {
//            e.printStackTrace();
//        } catch (ActionParameterException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public OrderLog createWithdraw(Long uid, Long diamonds, OrderRecharge.PAY_METHOD payMethod, String payId, String payName) {
        return null;
    }

    @Override
    public OrderLog createVIPCardUseDiamond(Long uid, Long day) throws ResourceNotFoundException, BalanceNotEnoughException {
        // 查找资源
        VIPTable vipTable = vipTableService.findByDay(day);
//        if (vipTable == null)
//            throw new ResourceNotFoundException("Can not found Resource [VIP: " + day + " day ]");
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
    public String createVIPCardUseAliPay(Long uid, Long day, HttpServletRequest request) throws ResourceNotFoundException, ActionParameterException {
        VIPTable vipTable = vipTableService.findByDay(day);
        BigDecimal price = vipTable.getPrice();
        //
        String subject = "尤物圈VIP充值【 " + day + " 天】";
        String body = subject;
        String ip = util4IP.getIpAddress(request);
        String deviceInfo = request.getHeader("User-Agent");
        return placeOrder(genOrderID(), TradeModel.TYPE.BUYVIP, TradeModel.PAYMENT.ALIPAY, uid, price, subject, body, ip, deviceInfo, "" + day);
    }

    @Override
    public String createVIPCardUseWeChat(Long uid, Long day, HttpServletRequest request) throws ResourceNotFoundException, ActionParameterException {
        VIPTable vipTable = vipTableService.findByDay(day);
        BigDecimal price = vipTable.getPrice();
        //
        String subject = "尤物圈VIP充值【 " + day + " 天】";
        String body = subject;
        String ip = util4IP.getIpAddress(request);
        String deviceInfo = request.getHeader("User-Agent");
        return placeOrder(genOrderID(), TradeModel.TYPE.BUYVIP, TradeModel.PAYMENT.WECHAT, uid, price, subject, body, ip, deviceInfo, "" + day);
    }

    @Override
    public String createActivity(Long uid, Long activityId, TradeModel.PAYMENT payment, HttpServletRequest request) throws ResourceNotFoundException, ActionNotSupportException, ActionParameterException {
        Activity activity = activityService.findOne(activityId);
        if (activity == null) throw new ResourceNotFoundException("无此活动可以报名支付");
        BigDecimal price = activity.getPrice();
        if (price == null || price.doubleValue() == 0.0) throw new ActionNotSupportException("不可以支付 0 元的账单");
        String subject = "【尤物圈活动报名】" + activity.getTitle();
        String body = subject;
        String ip = util4IP.getIpAddress(request);
        String deviceInfo = request.getHeader("User-Agent");
        return placeOrder(genOrderID(), TradeModel.TYPE.ACTIVITY, payment, uid, price, subject, body, ip, deviceInfo, "");
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
     * @throws AmountCannotBeNegetiveException
     * @throws ActionNotSupportException       资源设定异常，价格不对（负数？）
     */
    @Transactional
    @Override
    public RewardOrder createReward(Long uid, Long herUid, Long rewardResourceId, Integer amount) throws BalanceNotEnoughException, ResourceNotFoundException, AmountCannotBeNegetiveException, ActionNotSupportException {
        // 判断资源是否存在
        Reward reward = rewardService.findOne(rewardResourceId); // throw ResourceNotFoundException
        // 计算总价
        if (amount <= 0) throw new AmountCannotBeNegetiveException();
        Long price = reward.getDiamonds() * amount;
        String orderID = genOrderID();
        // 观看者用户扣钱
        balanceService.update(orderID, uid, OrderLog.EVENT.REWARD_EXPENSE, price);
        // 收钱
        // 发布者用户收钱 （百分比配）
        Long transactionPrice = (long) (price * globalConfigurerService.getUserDiamondsPercent());
        balanceService.update(orderID, herUid, OrderLog.EVENT.REWARD_RECEIVE, transactionPrice);
        // 记录订单（送礼单独的订单）
        RewardOrder rewardOrder = new RewardOrder();
        rewardOrder.setOrderId(orderID);
        rewardOrder.setCreateTime(new Date());
        rewardOrder.setDiamonds(price);
        rewardOrder.setUid(uid);
        rewardOrder.setHerUid(herUid);
        rewardOrder.setRewardResourceId(rewardResourceId);
        rewardOrder = rewardOrderService.save(rewardOrder);
        // 通知
        applicationContext.publishEvent(new ReceiveRewardEvent(this, herUid, uid, "", rewardResourceId, reward.getName(), amount, price, transactionPrice, orderID));
        return rewardOrder;
    }

    @Override
    public GiftOrder createSendGift(Long uid, Long herUid, Long resourceId, Integer amount) throws BalanceNotEnoughException, ActionNotSupportException, ResourceNotFoundException, AmountCannotBeNegetiveException {
        // 判断资源是否存在
        Gift gift = giftService.findOne(resourceId);
        // 计算总价
        if (amount <= 0) throw new AmountCannotBeNegetiveException();
        Long price = gift.getDiamonds() * amount;
        String orderID = genOrderID();
        // 观看者用户扣钱
        balanceService.update(orderID, uid, OrderLog.EVENT.SEND_GIFT, price);
        // address

        // 记录订单（送礼单独的订单）
        GiftOrder giftOrder = new GiftOrder();
        giftOrder.setOrderId(orderID);
        giftOrder.setCreateTime(new Date());
        giftOrder.setDiamonds(price);
        giftOrder.setUid(uid);
        giftOrder.setHerUid(herUid);
        giftOrder.setRewardResourceId(resourceId);
        giftOrder = giftOrderService.save(giftOrder);
        // 通知
        applicationContext.publishEvent(new ReceiveGiftEvent(this, herUid, uid, "", resourceId, gift.getName(), amount, price, price, orderID));
        return giftOrder;
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
        if (resourceAuthService.canVisitPublish(uid, publishId))
            throw new ResourceAlreadyActivedException();
        Long diamonds = publish.getUnlockPrice().longValue();
        String orderID = genOrderID();
        // 观看者用户扣钱
        OrderLog log = balanceService.update(orderID, uid, OrderLog.EVENT.UNLOCK_PUBLISH, diamonds);
        // 发布者用户收钱 （百分比配）
        Long transactionDiamonds = (long) (diamonds * globalConfigurerService.getUserDiamondsPercent());
        balanceService.update(orderID, herUid, OrderLog.EVENT.RECEIVE_PUBLISH, transactionDiamonds);
        // 激活权限
        resourceAuthService.activePublishResource(uid, publishId, timeInterval_Publish); // 默认一天
        // publish record 紀錄每個動態的收入
        Long pdiamonds = publish.getReceivedDiamonds();
        if (pdiamonds == null) pdiamonds = 0L;
        publish.setReceivedDiamonds(pdiamonds + diamonds);
        publishService.save(publish);
        return log;
    }

    @Override
    public OrderLog createUnlockHomePageVideo(Long uid, Long videoId) throws ResourceNotFoundException, BalanceNotEnoughException, ResourceAlreadyActivedException, ActionNotSupportException {
        HomePageVideo video = homePageVideoService.findOne(videoId);
        if (video == null)
            throw new ResourceNotFoundException("視頻不存在！");
        Long herUid = video.getUid();
        // 查看是否激活
        if (resourceAuthService.canVisitVideo(uid, videoId))
            throw new ResourceAlreadyActivedException();
        Long diamonds = video.getDiamonds();
        String orderID = genOrderID();
        OrderLog log = balanceService.update(orderID, uid, OrderLog.EVENT.UNLOCK_VIDEO, diamonds);
        // 發布者收款
        Long transactionDiamonds = (long) (diamonds * globalConfigurerService.getUserDiamondsPercent());
        balanceService.update(orderID, herUid, OrderLog.EVENT.RECEIVE_VIDEO, transactionDiamonds);
        // 激活权限
        resourceAuthService.activeVideoResource(uid, videoId, timeInterval_Publish); // 默认一天
        // publish record
        Long receivedDiamonds = video.getReceivedDiamonds();
        if (receivedDiamonds == null) receivedDiamonds = 0L;
        video.setReceivedDiamonds(receivedDiamonds + diamonds);
        homePageVideoService.save(video);
        return log;
    }

    @Override
    public OrderLog createUnlockWeChatID(Long uid, Long herUid) throws BalanceNotEnoughException, WeChatNotSetException, SMSSendFailureException, GlobalConfigSettingException, ActionParameterException {
        if (uid == null || herUid == null || uid == herUid)
            throw new ActionParameterException("user ids can not be empty or same");
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
            balanceService.update(orderId, herUid, OrderLog.EVENT.RECEIVE_WECHAT, diamonds);
        } catch (ActionNotSupportException e) {
            e.printStackTrace(); // 不可能的
        }
        return log;
    }

    @Override
    public String createUnlockPhoneNumber(Long uid, Long herUid) throws BalanceNotEnoughException, GlobalConfigSettingException, PhoneNumberNetSetException, ActionParameterException {
        if (uid == null || herUid == null || uid == herUid)
            throw new ActionParameterException("user ids can not be empty or same");
        // 查询手机号
        String phone = userReactService.getPhone(herUid);
        if (phone == null)
            throw new PhoneNumberNetSetException(herUid);

        // 记录账单
        Long diamonds = globalConfigurerService.getUnlockWeChat();
        String orderId = genOrderID();
        try {
            // 观看者用户扣钱
            balanceService.update(orderId, uid, OrderLog.EVENT.UNLOCK_PHONE, diamonds);
        } catch (ActionNotSupportException e) {
            throw new GlobalConfigSettingException("系统设置异常！手机号码解锁金额设定错误");
        }

        // 发布者用户收钱 （百分比配）
        diamonds = (long) (diamonds * globalConfigurerService.getUserDiamondsPercent());
        try {
            balanceService.update(orderId, herUid, OrderLog.EVENT.RECEIVE_PHONE, diamonds);
        } catch (ActionNotSupportException e) {
            e.printStackTrace(); // 不可能的
        }
        return phone;
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


    /**
     * @param orderId
     * @param payMethod
     * @param uid
     * @param price
     * @param subject
     * @param body
     * @param extraData if need
     * @return
     * @throws ActionParameterException
     */
    private String placeOrder(String orderId, TradeModel.TYPE type, TradeModel.PAYMENT payMethod, Long uid, BigDecimal price, String subject, String body, String ipAddress, String deviceInfo, String extraData) throws ActionParameterException {
        // 持久化
        TradeModel trade = new TradeModel();
        trade.setOrderId(orderId);
        trade.setStatus(TradeModel.TRADE_STATUS.WAIT_BUYER_PAY);
        trade.setBody(body);
        trade.setSubject(subject);
        trade.setCreateTime(new Date());
        trade.setUid(uid);
        trade.setPrice(price);
        trade.setExtraData(extraData);
        trade.setIpAddress(ipAddress);
        trade.setType(type); // 交易类型
        if (payMethod == TradeModel.PAYMENT.ALIPAY) {
            // 返回支付宝创建的订单 String 到客户端即可
            trade.setPayment(TradeModel.PAYMENT.ALIPAY);
            tradeService.save(trade);
            return aliPayService.createOrder(orderId, price, subject, body);
        }
        if (payMethod == TradeModel.PAYMENT.WECHAT) {
            trade.setPayment(TradeModel.PAYMENT.WECHAT);
            tradeService.save(trade);
            try {
                return wxPayService.createOrder(orderId, price, body, ipAddress, deviceInfo);
            } catch (IOException e) {
                return "创建失败！服务器异常";
            }
        }
        return null;
    }

    private String genOrderID() {
        SimpleDateFormat format = dateFormatterService.getyyyyMMddHHmmssS();
        String dateStr = format.format(new Date());
        return dateStr + (new Random().nextInt(900) + 100);
    }

    @Autowired
    private DateFormatterService dateFormatterService;
}
