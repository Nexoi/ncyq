package com.seeu.third.push;

import com.seeu.third.exception.PushException;

import java.util.Map;

public interface PushService {

    /**
     * 系统通知，每个用户都会收到
     *
     * @param text    描述内容
     * @param linkUrl 跳转的链接
     */
    void sysPush(String text, String linkUrl, Map extra) throws PushException;


    void singlePush(Long uid, String text, String linkUrl, Map extra) throws PushException ;

    /**
     * 点赞
     *
     * @param herUid
     * @param uid
     * @param nickname
     * @param headIconUrl
     * @param imgUrl
     * @param publishId
     */
    void likePublish(Long herUid, Long uid, String nickname, String headIconUrl, Long publishId, String imgUrl) throws PushException ;

    /**
     * 评论
     *
     * @param uid         评论者 UID
     * @param herUid      被评论的 UID
     * @param headIconUrl 评论者头像
     * @param publishId   动态 ID
     * @param text        评论内容（前 n 个字）
     * @param imgUrl      样例图片
     */
    void commentPublish(Long herUid, Long uid, String nickname, String headIconUrl, Long publishId, String text, String imgUrl) throws PushException ;
}
