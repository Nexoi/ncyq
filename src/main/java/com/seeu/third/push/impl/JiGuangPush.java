package com.seeu.third.push.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.seeu.third.exception.PushException;
import com.seeu.third.push.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JiGuangPush implements PushService {
    @Value("${jiguang.push.appkey}")
    private String APPKEY;
    @Value("${jiguang.push.master_secret}")
    private String MASTER_SECRET;

    @Autowired
    private JPushClient jPushClient;

    @Bean
    public JPushClient jPushClient() {
        return new JPushClient(MASTER_SECRET, APPKEY);
    }

    private Logger logger = LoggerFactory.getLogger(JiGuangPush.class);

    public void push(PushPayload pushPayload) throws PushException {
        try {
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            System.out.println("code:::::" + pushResult.statusCode);
        } catch (APIConnectionException e) {
//            e.printStackTrace();
            logger.warn("JiGuang Exception" + e.getMessage());
            throw new PushException(e.getMessage());
        } catch (APIRequestException e) {
//            e.printStackTrace();
            logger.warn("JiGuang Exception" + e.getMessage());
            // throw new PushException(e.getMessage());
        }
    }

    @Override
    public void sysPush(String text, String linkUrl, Map extra) throws PushException {
        if (extra == null) extra = new HashMap();
        extra.put("type", "systemNotify");
        extra.put("linkUrl", linkUrl);
        extra.put("text", text);

        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all()) // 所有人通知
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert("系统通知")
                                .setTitle("系统通知")
                                .addExtras(extra)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert("系统通知")
                                .addExtras(extra)
                                .build())
                        .build())
                .setMessage(Message.newBuilder()
                        .setTitle("系统通知")
                        .setMsgContent(JSON.toJSONString(extra))
                        .build())
                .build();
        push(pushPayload);
    }

    @Async
    @Override
    public void singlePush(Long uid, String text, String linkUrl, Map extra) throws PushException {
        if (extra == null) extra = new HashMap();
        extra.put("type", "notify");
        extra.put("linkUrl", linkUrl);
        extra.put("text", text);

        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(String.valueOf(uid)))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(text)
                                .setTitle("通知")
                                .addExtras(extra)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setSound("default")
                                .setAlert(text)
                                .addExtras(extra)
                                .build())
                        .build())
                .setMessage(Message.newBuilder()
                        .setTitle(text)
                        .setMsgContent(JSON.toJSONString(extra))
                        .build())
                .build();
        push(pushPayload);
    }

    @Async
    @Override
    public void likePublish(Long herUid, Long uid, String nickname, String headIconUrl, Long publishId, String imgUrl) throws PushException {
        Map extra = new HashMap();
        extra.put("type", "like");
        extra.put("uid", String.valueOf(uid));
        extra.put("nickname", nickname);
        extra.put("headIconUrl", headIconUrl);
        extra.put("publishId", String.valueOf(publishId));
        extra.put("imgUrl", imgUrl);

        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(String.valueOf(herUid)))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(nickname + "点赞了你的动态")
                                .setTitle(nickname + "点赞了你的动态")
                                .addExtras(extra)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setSound("default")
                                .setAlert(nickname + "点赞了你的动态")
                                .addExtras(extra)
                                .build())
                        .build())
                .setMessage(Message.newBuilder()
                        .setTitle(nickname + "点赞了你的动态")
                        .setMsgContent(JSON.toJSONString(extra))
                        .build())
                .build();
        push(pushPayload);
    }

    @Async
    @Override
    public void commentPublish(Long herUid, Long uid, String nickname, String headIconUrl, Long publishId, String text, String imgUrl) throws PushException {
        Map extra = new HashMap();
        extra.put("type", "comment");
        extra.put("uid", String.valueOf(uid));
        extra.put("nickname", nickname);
        extra.put("headIconUrl", headIconUrl);
        extra.put("publishId", String.valueOf(publishId));
        extra.put("imgUrl", imgUrl);
        extra.put("text", text);

        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(String.valueOf(herUid)))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(nickname + "评论了你的动态")
                                .setTitle(nickname + "评论了你的动态")
                                .addExtras(extra)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setSound("default")
                                .setAlert(nickname + "评论了你的动态")
                                .addExtras(extra)
                                .build())
                        .build())
                .setMessage(Message.newBuilder()
                        .setTitle(nickname + "评论了你的动态")
                        .setMsgContent(JSON.toJSONString(extra))
                        .build())
                .build();
        push(pushPayload);
    }
}
