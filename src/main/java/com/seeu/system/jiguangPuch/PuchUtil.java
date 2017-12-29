package com.seeu.system.jiguangPuch;


import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;


/**
 * 极光推送工具类
 *
 * @author Scary
 */
public class PuchUtil {

    private static final Logger log = Logger.getLogger(PuchUtil.class);

    /**
     * 极光推送APP_KEY
     */
    @Value("push.app.key")
    private String JG_APP_KEY;

    /**
     * 极光推送MASTER_SECRET
     */
    @Value("push.master.secret")
    private String JG_MASTER_SECRET;

    /**
     * true-推送生产环境 false-推送开发环境（测试使用参数）
     */
    @Value("push.push.test")
    private String JG_PUSH_TEST;

    /**
     * 消息在JPush服务器的失效时间（测试使用参数）
     */
    @Value("push.time.live")
    private String JG_TIME_LIVE;


    /**
     * 生成极光推送对象PushPayload（采用java SDK）
     *
     * @param userNoA 被通知用户的用户号
     * @param userNoB 互动用户的用户号(若没有则传null)
     * @param msg1    通知内容(通知-apns)
     * @param msg2    通知内容(自定义消息-jpush)
     * @return
     */
    public PushPayload buildPushObject_android_ios_alias_alert(String userNoA, String userNoB, String msg1, String msg2) {

        boolean flag = false;
        if ("true".equals(JG_PUSH_TEST)) {
            flag = true;
        }

        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(userNoA))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .addExtra("type", "infomation")
                                .setAlert(msg1)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .addExtra("type", "infomation")
                                .setAlert(msg1)
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(flag)//true-推送生产环境 false-推送开发环境（测试使用参数）
                        .setTimeToLive(Integer.parseInt(JG_TIME_LIVE))//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(msg2)
                        .addExtra("type", "infomation")
                        .build())
                .build();
    }


    /**
     * 极光推送方法(采用java SDK)
     *
     * @param userNoA 被通知用户的用户号
     * @param userNoB 互动用户的用户号(若没有则传null)
     * @param msg1    通知内容(通知-apns)
     * @param msg2    通知内容(自定义消息-jpush)
     * @return
     */
    public PushResult push(String userNoA, String userNoB, String msg1, String msg2) {

        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(JG_MASTER_SECRET, JG_APP_KEY, null, clientConfig);
        PushPayload payload = buildPushObject_android_ios_alias_alert(userNoA, userNoB, msg1, msg2);
        try {
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            return null;
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Comment: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            return null;
        }
    }

}
