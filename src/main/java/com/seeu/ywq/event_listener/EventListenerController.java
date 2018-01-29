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
import com.seeu.ywq.event_listener.yellowpicture.YellowEvent;
import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.message.model.PersonalMessage;
import com.seeu.ywq.message.service.PersonalMessageService;
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
    @Autowired
    private PersonalMessageService personalMessageService;

    @EventListener
    public void clickMe(ClickLikeEvent event) {
        // 任务
        dayFlushTaskService.update(event.getUid(), TaskCategory.CATEGORY.like);
        // 推送
        try {
            // 持久化
            JSONObject jo = new JSONObject();
            jo.put("uid", event.getUid());
            jo.put("herUid", event.getHerUid());
            jo.put("nickname", event.getNickname());
            jo.put("headIconUrl", event.getHeadIconUrl());
            jo.put("publishId", event.getPublishId());
            jo.put("imgUrl", event.getImgUrl());

            PersonalMessage message = new PersonalMessage();
            message.setCreateTime(new Date());
            message.setType(PersonalMessage.TYPE.like);
            message.setUid(event.getHerUid());
            message.setExtraJson(jo.toJSONString()); // JSON.toJSONString(event);
            personalMessageService.add(message);
            // 推送
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
            // 持久化
            JSONObject jo = new JSONObject();
            jo.put("uid", event.getUid());
            jo.put("herUid", event.getHerUid());
            jo.put("nickname", event.getNickname());
            jo.put("headIconUrl", event.getHeadIconUrl());
            jo.put("publishId", event.getPublishId());
            jo.put("imgUrl", event.getImgUrl());

            PersonalMessage message = new PersonalMessage();
            message.setCreateTime(new Date());
            message.setType(PersonalMessage.TYPE.comment);
            message.setUid(event.getHerUid());
            message.setExtraJson(jo.toJSONString());
            personalMessageService.add(message);
            // 推送
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

            // 持久化
            PersonalMessage message = new PersonalMessage();
            message.setCreateTime(new Date());
            message.setType(PersonalMessage.TYPE.reward);
            message.setUid(event.getUid());
            message.setExtraJson(JSON.toJSONString(event));
            personalMessageService.add(message);
            // 推送
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

            // 持久化
            PersonalMessage message = new PersonalMessage();
            message.setCreateTime(new Date());
            message.setType(PersonalMessage.TYPE.gift);
            message.setUid(event.getUid());
            message.setExtraJson(JSON.toJSONString(event));
            personalMessageService.add(message);
            // 推送
            pushService.singlePush(event.getUid(), null, "", null);
        } catch (PushException e) {
            e.printStackTrace();
            logger.warn("通知失败！ReceiveReward 【OrderID:" + event.getOrderId() + "】");
        }
    }

    @EventListener
    public void yellowPicture(YellowEvent event){

        try {
            Map extraData = new HashMap();
            extraData.put("message", "动态图片含有色情信息");
            extraData.put("score", event.getScore());

            // 持久化
            PersonalMessage message = new PersonalMessage();
            message.setCreateTime(new Date());
            message.setType(PersonalMessage.TYPE.yellowPicture);
            message.setUid(event.getUid());
            message.setExtraJson(JSON.toJSONString(event));
            personalMessageService.add(message);
            // 推送
            pushService.singlePush(event.getUid(), "您的图片含有色情信息，已被删除！", null, extraData);
        } catch (PushException e) {
            e.printStackTrace();
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
