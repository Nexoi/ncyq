package com.seeu.ywq.event_listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seeu.third.exception.PushException;
import com.seeu.third.push.PushService;
import com.seeu.ywq.event_listener.order_event.ReceiveGiftEvent;
import com.seeu.ywq.event_listener.order_event.ReceiveRewardEvent;
import com.seeu.ywq.event_listener.publish_react.ClickLikeEvent;
import com.seeu.ywq.event_listener.publish_react.PublishCommentEvent;
import com.seeu.ywq.event_listener.publish_react.ShareEvent;
import com.seeu.ywq.event_listener.task.SignInTodayEvent;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.OrderLog;
import com.seeu.ywq.pay.service.BalanceService;
import com.seeu.ywq.task.model.DayFlushTask;
import com.seeu.ywq.task.model.TaskCategory;
import com.seeu.ywq.task.service.DayFlushTaskService;
import com.seeu.ywq.user.service.AddressService;
import com.seeu.ywq.utils.DateFormatterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class EventListenerController {
    Logger logger = LoggerFactory.getLogger(EventListenerController.class);
    @Autowired
    private PushService pushService;
    @Autowired
    private DayFlushTaskService dayFlushTaskService;


    @EventListener
    public void clickMe(ClickLikeEvent event) {
        // 任务
        dayFlushTaskService.update(event.getUid(), TaskCategory.CATEGORY.like);
        // 推送
        try {
            pushService.likePublish(
                    event.getHerUid(),
                    event.getUid(),
                    event.getNickname(),
                    event.getHeadIconUrl(),
                    event.getPublishId(),
                    event.getImgUrl()
            );
            logger.info("点赞成功！");
        } catch (PushException e) {
            e.printStackTrace();
            logger.warn("点赞失败！");
        }
    }

    @EventListener
    public void commentPublish(PublishCommentEvent event) {
        // 任务
        dayFlushTaskService.update(event.getUid(), TaskCategory.CATEGORY.comment);
        // 推送
        try {
            pushService.commentPublish(
                    event.getHerUid(),
                    event.getUid(),
                    event.getNickname(),
                    event.getHeadIconUrl(),
                    event.getPublishId(),
                    event.getText(),
                    event.getImgUrl()
            );
            logger.info("评论成功！");
        } catch (PushException e) {
            e.printStackTrace();
            logger.warn("评论失败！");
        }
    }

    @EventListener
    public void shareLink(ShareEvent event) {
        // 任务
        dayFlushTaskService.update(event.getUid(), TaskCategory.CATEGORY.share);
    }

    @Autowired
    private AddressService addressService;

    @EventListener
    public void receiveReward(ReceiveRewardEvent event) {
        try {
            String text = "用户%nickname%【ID：%id%】送给您了%amount%朵%giftName%，已转换为钻石：%price%颗";
            text = text.replace("%nickname%", event.getHisNickname())
                    .replace("%id%", "" + event.getHisUid())
                    .replace("%amount%", "" + event.getAmount())
                    .replace("%giftName%", event.getRewardResourceName())
                    .replace("%price%", "" + event.getTransactionalDiamonds());
            Map map = new HashMap();
            map.put("info", JSON.toJSON(event));
            pushService.singlePush(event.getUid(), text, "", map);
        } catch (PushException e) {
            e.printStackTrace();
            logger.warn("通知失败！ReceiveReward 【OrderID:" + event.getOrderId() + "】");
        }
    }

    @EventListener
    public void receiveGift(ReceiveGiftEvent event) {
        try {
//            String text = "用户%nickname%【ID：%id%】送给您了%amount%朵%giftName%，已转换为钻石：%price%颗";
//            text = text.replace("%nickname%", event.getHisNickname())
//                    .replace("%id%", "" + event.getHisUid())
//                    .replace("%amount%", "" + event.getAmount())
//                    .replace("%giftName%", event.getRewardResourceName())
//                    .replace("%price%", "" + event.getTransactionalDiamonds());
//            Map map = new HashMap();
//            map.put("info", JSON.toJSON(event));
            pushService.singlePush(event.getUid(), null, "", null);
        } catch (PushException e) {
            e.printStackTrace();
            logger.warn("通知失败！ReceiveReward 【OrderID:" + event.getOrderId() + "】");
        }
    }


    @Autowired
    private DateFormatterService dateFormatterService;
    @Autowired
    private BalanceService balanceService;

    @EventListener
    public void signInTodayEvent(SignInTodayEvent event) {
        Long uid = event.getUid();
        // 给他加钱
        try {
            balanceService.update(genOrderID(), uid, OrderLog.EVENT.DAY_SIGN_IN, 1L);
        } catch (BalanceNotEnoughException e) {
            e.printStackTrace();
        } catch (ActionNotSupportException e) {
            e.printStackTrace();
        }
    }

    private String genOrderID() {
        SimpleDateFormat format = dateFormatterService.getyyyyMMddHHmmssS();
        String dateStr = format.format(new Date());
        return dateStr + (new Random().nextInt(900) + 100);
    }
}
