package com.seeu.ywq.event_listener;

import com.seeu.third.exception.PushException;
import com.seeu.third.push.PushService;
import com.seeu.ywq.event_listener.publish_react.ClickLikeEvent;
import com.seeu.ywq.event_listener.publish_react.PublishCommentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventListenerController {
    Logger logger = LoggerFactory.getLogger(EventListenerController.class);
    @Autowired
    private PushService pushService;

    @EventListener
    public void clickMe(ClickLikeEvent event) {
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
}
