package com.seeu.ywq.trend.service.impl;

import com.seeu.ywq.event_listener.publish_react.ClickLikeEvent;
import com.seeu.ywq.event_listener.publish_react.PublishCommentEvent;
import com.seeu.ywq.resource.model.*;
import com.seeu.ywq.trend.dvo.PublishVO;
import com.seeu.ywq.trend.dvo.PublishVOPicture;
import com.seeu.ywq.trend.dvo.PublishVOVideo;
import com.seeu.ywq.trend.repository.PublishCommentRepository;
import com.seeu.ywq.trend.repository.PublishLikedUserRepository;
import com.seeu.ywq.trend.repository.PublishRepository;
import com.seeu.ywq.resource.service.*;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.model.PublishComment;
import com.seeu.ywq.trend.model.PublishLikedUser;
import com.seeu.ywq.trend.model.PublishLikedUserPKeys;
import com.seeu.ywq.trend.service.PublishCommentService;
import com.seeu.ywq.trend.service.PublishLikedUserService;
import com.seeu.ywq.trend.service.PublishService;
import com.seeu.ywq.trend.service.PublishVideoService;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.user.service.UserPictureService;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class PublishServiceImpl implements PublishService {
    @Resource
    private PublishRepository publishRepository;
    @Autowired
    private UserPictureService userPictureService;
    @Autowired
    private PublishLikedUserService publishLikedUserService;
    @Autowired
    private PublishCommentService publishCommentService;
    @Autowired
    private PublishVideoService publishVideoService;
    @Autowired
    private ResourceAuthService resourceAuthService;
    @Autowired
    private UserInfoService userInfoService;

    /* 以下两个数据源只在此类使用 **/
    @Resource
    private PublishLikedUserRepository publishLikedUserRepository;
    @Resource
    private PublishCommentRepository publishCommentRepository;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Publish findOne(Long publishId) {
        return publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
    }

    @Override
    public Publish findOne(Long uid, Long publishId) {
        return publishRepository.findByIdAndUidAndStatus(publishId, uid, Publish.STATUS.normal);
    }

    @Override
    public Publish save(Publish publish) {
        if (publish == null) return null;
        // 用户发布数量加一
        if (publish.getUid() != null)
            userInfoService.publishPlusOne(publish.getUid());
        return publishRepository.save(publish);
    }

    @Override
    public Page findAllByUid(Long uid, boolean canVisitClosedResource, Pageable pageable) {
        Page page = publishRepository.findAllByUidAndStatus(uid, Publish.STATUS.normal, pageable);
        List<Publish> publishes = page.getContent();
        if (publishes == null || publishes.size() == 0)
            return page;
        List<PublishVO> vos = transferToVO(publishes, canVisitClosedResource);
        return new PageImpl(vos, pageable, page.getTotalElements());
    }


    @Override
    public PublishVO findOneByPublishId(Long publishId, boolean canVisitClosedResource) {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null) return null;
        return transferToVO(publish, canVisitClosedResource);
    }

    ///////////////////////////////////////////************** transfer operations ***************////////////////////////////////////////////////////

    @Override
    public PublishVO transferToVO(Publish publish, boolean canVisitClosedResource) {
        if (publish == null) return null;
        switch (publish.getType()) {
            case picture:
                PublishVOPicture vop = new PublishVOPicture();
                BeanUtils.copyProperties(publish, vop);
                vop.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vop.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vop.setComments(publishCommentService.transferToVO(publish.getComments()));
                vop.setCoverPictureUrl(publish.getPictures() == null || publish.getPictures().size() == 0 ? null : userPictureService.transferToVO(publish.getPictures().get(0), canVisitClosedResource));
                vop.setPictures(userPictureService.transferToVO(publish.getPictures(), canVisitClosedResource));
                return vop;
            case video:
                PublishVOVideo vod = new PublishVOVideo();
                BeanUtils.copyProperties(publish, vod);
                vod.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vod.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vod.setComments(publishCommentService.transferToVO(publish.getComments()));
                vod.setVideo(publishVideoService.transferToVO(publish.getVideo(), canVisitClosedResource));
                // TODO video 权限得加
                return vod;
            case word:
            default:
                PublishVO vo = new PublishVO();
                BeanUtils.copyProperties(publish, vo);
                vo.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vo.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vo.setComments(publishCommentService.transferToVO(publish.getComments()));
                return vo;
        }
    }

    @Override
    public List<PublishVO> transferToVO(List<Publish> publishs, boolean canVisitClosedResource) {
        List<PublishVO> vos = new ArrayList<>();
        for (Publish publish : publishs) {
            if (publish == null) continue;
            vos.add(transferToVO(publish, canVisitClosedResource));
        }
        return vos;
    }

    ///////////////////////////////////////////************** other operations ***************////////////////////////////////////////////////////

    @Override
    public PublishVO viewIt(Long publishId) {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null) return null;
        publishRepository.viewItOnce(publishId);
        return transferToVO(publish, false);
    }

    @Override
    public PublishVO viewIt(Long publishId, Long uid) {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null) return null;
        publishRepository.viewItOnce(publishId);
        return transferToVO(publish, resourceAuthService.canVisit(uid, publishId));
    }

    @Transactional
    @Override
    public STATUS deletePublish(Long publishId) {
        Publish publish = publishRepository.findOne(publishId);
        if (publish == null)
            return STATUS.not_found_publish;
        // 删除全部信息（包含点赞、评论）
        publishRepository.delete(publishId);
        publishLikedUserRepository.deleteAllByPublishId(publishId);
        publishCommentRepository.deleteAllByPublishId(publishId);
        // 用户发布数量减一
        if (publish.getUid() != null)
            userInfoService.publishMinsOne(publish.getUid());
        return STATUS.success;
    }

    @Override
    public Page<PublishLikedUser> listLikedUser(Long publishId, Pageable pageable) {
        return publishLikedUserRepository.findAllByPublishId(publishId, pageable);
    }

    @Override
    public STATUS likeIt(Long publishId, UserLogin user) {
        Publish publish = publishRepository.findOne(publishId);
        if (publish == null)
            return STATUS.not_found_publish;
        // 是否点赞过
        if (publishLikedUserRepository.exists(new PublishLikedUserPKeys(publishId, user.getUid())))
            return STATUS.existed_not_modify;
        PublishLikedUser like = new PublishLikedUser();
        like.setUid(user.getUid());
        like.setHeadIconUrl(user.getHeadIconUrl());
        like.setPublishId(publishId);
        publishLikedUserRepository.save(like);
        publishRepository.likeItOnce(publishId);
        // 通知
        String imgUrl = null;
        if (publish.getPictures() != null && publish.getPictures().size() > 0) {
            Image image = publish.getPictures().get(0).getImageOpen(); // 用户自己收到点赞事件
            imgUrl = image.getThumbImage200pxUrl();
        }
        applicationContext.publishEvent(new ClickLikeEvent(this, publish.getUid(), user.getUid(), user.getNickname(), user.getHeadIconUrl(), publishId, imgUrl));
        return STATUS.success;
    }

    @Override
    public STATUS dislikeIt(Long publishId, Long uid) {
        if (!publishRepository.exists(publishId))
            return STATUS.not_found_publish;
        // 是否点赞过
        PublishLikedUserPKeys PK = new PublishLikedUserPKeys(publishId, uid);
        if (!publishLikedUserRepository.exists(PK))
            return STATUS.not_existed;
        publishLikedUserRepository.delete(PK);
        publishRepository.dislikeItOnce(publishId);
        return STATUS.success;
    }

    @Override
    public PublishComment getComment(Long commentId) {
        PublishComment comment = publishCommentRepository.findOne(commentId);
        return comment;
    }

    @Override
    public Page<PublishComment> listComments(Long publishId, Pageable pageable) {
        return publishCommentRepository.findAllByPublishIdAndFatherIdIsNull(publishId, pageable);
    }

    @Override
    public STATUS commentIt(Long publishId, Long fatherId, UserLogin user, String text) {
        Publish publish = publishRepository.findOne(publishId);
        if (publish == null)
            return STATUS.not_found_publish;
        if (fatherId != null && !publishCommentRepository.exists(fatherId))
            return STATUS.not_existed;
        PublishComment comment = new PublishComment();
        comment.setPublishId(publishId);
        comment.setUid(user.getUid());
        comment.setUsername(user.getNickname());
        comment.setHeadIconUrl(user.getHeadIconUrl());
        comment.setFatherId(fatherId == null || fatherId < 1 ? null : fatherId);
        comment.setText(text);
        comment.setCommentDate(new Date());
        publishCommentRepository.save(comment);
        publishRepository.commentItOnce(publishId);
        // 通知
        String imgUrl = null;
        if (publish.getPictures() != null && publish.getPictures().size() > 0) {
            Image image = publish.getPictures().get(0).getImageOpen(); // 用户自己收到点赞事件
            imgUrl = image.getThumbImage200pxUrl();
        }
        applicationContext.publishEvent(new PublishCommentEvent(this, publish.getUid(), user.getUid(), user.getNickname(), user.getHeadIconUrl(), publishId, text, imgUrl));
        return STATUS.success;
    }

    @Override
    public STATUS deleteComment(Long commentId) {
        PublishComment comment = publishCommentRepository.findOne(commentId);
        if (comment == null)
            return STATUS.not_existed;
        publishCommentRepository.delete(commentId);
        publishRepository.disCommentItOnce(comment.getPublishId());
        return STATUS.success;
    }

}
