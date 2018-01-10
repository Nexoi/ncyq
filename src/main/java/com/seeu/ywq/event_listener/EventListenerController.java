package com.seeu.ywq.event_listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seeu.third.exception.PushException;
import com.seeu.third.push.PushService;
import com.seeu.ywq.event_listener.order_event.ReceiveRewardEvent;
import com.seeu.ywq.event_listener.publish_react.ClickLikeEvent;
import com.seeu.ywq.event_listener.publish_react.PublishCommentEvent;
import com.seeu.ywq.task.model.DayFlushTask;
import com.seeu.ywq.task.service.DayFlushTaskService;
import com.seeu.ywq.user.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
        dayFlushTaskService.update(event.getUid(), DayFlushTask.TYPE.like);
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
        dayFlushTaskService.update(event.getUid(), DayFlushTask.TYPE.comment);
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
}
