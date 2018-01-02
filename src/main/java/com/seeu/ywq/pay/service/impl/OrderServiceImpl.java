package com.seeu.ywq.pay.service.impl;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.sms.SMSService;
import com.seeu.ywq.config.service.GlobalConfigurerService;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.model.OrderRecharge;
import com.seeu.ywq.pay.service.BalanceService;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.exception.PublishNotFoundException;
import com.seeu.ywq.exception.ResourceAlreadyActivedException;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.resource.repository.OrderLogRepository;
import com.seeu.ywq.trend.service.PublishService;
import com.seeu.ywq.resource.service.ResourceAuthService;
import com.seeu.ywq.userlogin.exception.WeChatNotSetException;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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

    @Value("${ywq.resource.interval.publish}")
    private Integer timeInterval_Publish;

    @Value("${ywq.sms.unlock_wechat}")
    private String unlockWeChatSMSText;

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
    public OrderLog createUnlockPublish(Long uid, Long publishId) throws PublishNotFoundException, BalanceNotEnoughException, ResourceAlreadyActivedException {
        // 判断资源
        Publish publish = publishService.findOne(publishId);
        if (publish == null)
            throw new PublishNotFoundException("动态不存在！");
        Long herUid = publish.getUid();
        // 查看是否在激活状态
        if (resourceAuthService.canVisit(uid, publishId))
            throw new ResourceAlreadyActivedException();
        Long diamonds = publish.getUnlockPrice().longValue();
        // 观看者用户扣钱
        balanceService.minus(uid, diamonds);
        // 记录订单
        OrderLog log = new OrderLog();
        log.setOrderId(genOrderID());
        log.setCreateTime(new Date());
        log.setDiamonds(diamonds);
        log.setEvent(OrderLog.EVENT.UNLOCK_PUBLISH);
        log.setType(OrderLog.TYPE.OUT);
        log.setUid(uid);
        log = orderLogRepository.save(log);
        // 发布者用户收钱 （百分比配）
        diamonds = (long) (diamonds * globalConfigurerService.getUserRewardDiamondsPercent());
        balanceService.plus(herUid, diamonds, OrderLog.EVENT.UNLOCK_PUBLISH);
        // 记录订单
        OrderLog log2 = new OrderLog();
        log2.setOrderId(genOrderID());
        log2.setCreateTime(new Date());
        log2.setDiamonds(diamonds);
        log2.setEvent(OrderLog.EVENT.UNLOCK_PUBLISH);
        log2.setType(OrderLog.TYPE.IN);
        log2.setUid(herUid);
        log2 = orderLogRepository.save(log2);
        // 激活权限
        resourceAuthService.activeResource(uid, publishId, timeInterval_Publish); // 默认一天
        return log;
    }

    @Override
    public OrderLog createUnlockWeChatID(Long uid, Long herUid) throws BalanceNotEnoughException, WeChatNotSetException, SMSSendFailureException {
        // 查询微信号
        String wechat = userReactService.getWeChatID(herUid);
        if (wechat == null)
            throw new WeChatNotSetException();
        // 查看自己的手机号码
        String myPhone = userReactService.getPhone(uid);
        if (myPhone == null)
            throw new SMSSendFailureException(myPhone, "手机号码为空，发送失败");
        // 发送短信
        smsService.send(myPhone, ("" + unlockWeChatSMSText).replace("%wechat%", wechat));
        Long diamonds = globalConfigurerService.getUnlockWeChat();
        // 观看者用户扣钱
        balanceService.minus(uid, diamonds);
        // 记录订单
        OrderLog log = new OrderLog();
        log.setOrderId(genOrderID());
        log.setCreateTime(new Date());
        log.setDiamonds(diamonds);
        log.setEvent(OrderLog.EVENT.UNLOCK_WECHAT);
        log.setType(OrderLog.TYPE.OUT);
        log.setUid(uid);
        log = orderLogRepository.save(log);
        // 发布者用户收钱 （百分比配）
        diamonds = (long) (diamonds * globalConfigurerService.getUserRewardDiamondsPercent());
        balanceService.plus(herUid, diamonds, OrderLog.EVENT.UNLOCK_WECHAT);
        // 记录订单
        OrderLog log2 = new OrderLog();
        log2.setOrderId(genOrderID());
        log2.setCreateTime(new Date());
        log2.setDiamonds(diamonds);
        log2.setEvent(OrderLog.EVENT.UNLOCK_WECHAT);
        log2.setType(OrderLog.TYPE.IN);
        log2.setUid(herUid);
        log2 = orderLogRepository.save(log2);
        return log;
    }

    @Override
    public OrderLog createBindShare(Long bindUid, Long diamonds) {
        diamonds = (long) (diamonds * globalConfigurerService.getBindUserShareDiamondsPercent());
        // 加钱
        balanceService.plus(bindUid, diamonds, OrderLog.EVENT.BIND_SHARE);
        // 日志
        OrderLog log = new OrderLog();
        log.setOrderId(genOrderID());
        log.setCreateTime(new Date());
        log.setDiamonds(diamonds);
        log.setEvent(OrderLog.EVENT.BIND_SHARE);
        log.setType(OrderLog.TYPE.IN);
        log.setUid(bindUid);
        log = orderLogRepository.save(log);
        return log;
    }

    @Override
    public Page<OrderLog> queryAll(Long uid, Pageable pageable) {
        return orderLogRepository.findAllByUid(uid, pageable);
    }

    private String genOrderID() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssS");
        String dateStr = format.format(new Date());
        return dateStr + (new Random().nextInt(900) + 100);
    }
}
