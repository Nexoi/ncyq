package com.seeu.ywq.release.service.impl;

import com.seeu.ywq.release.dvo.PublishVOPicture;
import com.seeu.ywq.release.dvo.PublishVO;
import com.seeu.ywq.release.dvo.PublishVOVideo;
import com.seeu.ywq.release.model.*;
import com.seeu.ywq.release.repository.PublishCommentRepository;
import com.seeu.ywq.release.repository.PublishLikedUserRepository;
import com.seeu.ywq.release.repository.PublishRepository;
import com.seeu.ywq.release.service.PublishCommentService;
import com.seeu.ywq.release.service.PublishLikedUserService;
import com.seeu.ywq.release.service.PublishService;
import com.seeu.ywq.release.service.UserPictureService;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Resource
    private PublishLikedUserRepository publishLikedUserRepository;
    @Resource
    private PublishCommentRepository publishCommentRepository;

    @Override
    public PublishVO findOneByPublishId(Long publishId, Long uid) {
        Publish publish = publishRepository.findByIdAndUidAndStatus(publishId, uid, Publish.STATUS.normal);
        if (publish == null) return null;
        //.. 如果为本人，则不会做权限数据库搜索
        return transferToVO(publish, uid);
    }

    @Override
    public Page findAllByUid(Long uid, Long myUid, Pageable pageable) {
        Page page = publishRepository.findAllByUidAndStatus(uid, Publish.STATUS.normal, pageable);
        List<Publish> publishes = page.getContent();
        if (publishes == null || publishes.size() == 0)
            return page;
        List<PublishVO> vos = transferToVO(publishes, myUid);
        return new PageImpl(vos, pageable, page.getTotalElements());
    }

    //【匿名】
    @Override
    public PublishVO findOneByPublishId(Long publishId) {
        Publish publish = publishRepository.findOne(publishId);
        if (publish == null) return null;
        return transferToVO(publish);
    }

    //【匿名】
    @Override
    public Page findAllByUid(Long uid, Pageable pageable) {
        Page page = publishRepository.findAllByUidAndStatus(uid, Publish.STATUS.normal, pageable);
        List<Publish> publishes = page.getContent();
        if (publishes == null || publishes.size() == 0)
            return page;
        List<PublishVO> vos = transferToVO(publishes);
        return new PageImpl(vos, pageable, page.getTotalElements());
    }

    ///////////////////////////////////////////************** transfer operations ***************////////////////////////////////////////////////////

    @Override
    public PublishVO transferToVO(Publish publish, Long uid) {
        if (publish == null) return null;

        switch (publish.getType()) {
            case picture:
                PublishVOPicture vop = new PublishVOPicture();
                BeanUtils.copyProperties(publish, vop);
                vop.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vop.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vop.setComments(publishCommentService.transferToVO(publish.getComments()));
                vop.setCoverPictureUrl(publish.getPictures() == null || publish.getPictures().size() == 0 ? null : userPictureService.transferToVO(publish.getPictures().get(0), uid));
                vop.setPictures(userPictureService.transferToVO(publish.getPictures(), uid));
                return vop;
            case video:
                PublishVOVideo vod = new PublishVOVideo();
                BeanUtils.copyProperties(publish, vod);
                vod.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vod.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vod.setComments(publishCommentService.transferToVO(publish.getComments()));
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
    public List<PublishVO> transferToVO(List<Publish> publishs, Long uid) {
        List<PublishVO> vos = new ArrayList<>();
        for (Publish publish : publishs) {
            if (publish == null) continue;
            vos.add(transferToVO(publish, uid));
        }
        return vos;
    }

    //【匿名】
    @Override
    public PublishVO transferToVO(Publish publish) {
        if (publish == null) return null;

        switch (publish.getType()) {
            case picture:
                PublishVOPicture vop = new PublishVOPicture();
                BeanUtils.copyProperties(publish, vop);
                vop.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vop.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vop.setComments(publishCommentService.transferToVO(publish.getComments()));
                vop.setCoverPictureUrl(publish.getPictures() == null || publish.getPictures().size() == 0 ? null : userPictureService.transferToVO(publish.getPictures().get(0)));
                vop.setPictures(userPictureService.transferToVO(publish.getPictures()));
                return vop;
            case video:
                PublishVOVideo vod = new PublishVOVideo();
                BeanUtils.copyProperties(publish, vod);
                vod.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vod.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vod.setComments(publishCommentService.transferToVO(publish.getComments()));

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

    //【匿名】
    @Override
    public List<PublishVO> transferToVO(List<Publish> publishs) {
        List<PublishVO> vos = new ArrayList<>();
        for (Publish publish : publishs) {
            if (publish == null) continue;
            vos.add(transferToVO(publish));
        }
        return vos;
    }

    ///////////////////////////////////////////************** other operations ***************////////////////////////////////////////////////////

    @Override
    public PublishVO viewIt(Long publishId) {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null) return null;
        publishRepository.viewItOnce(publishId);
        return transferToVO(publish);
    }

    @Override
    public PublishVO viewIt(Long publishId, Long uid) {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null) return null;
        publishRepository.viewItOnce(publishId);
        return transferToVO(publish, uid);
    }

    @Override
    public STATUS deletePublish(Long publishId) {
        if (!publishRepository.exists(publishId))
            return STATUS.not_found_publish;
        // 删除全部信息（包含点赞、评论）
        publishRepository.delete(publishId);
        publishLikedUserRepository.deleteAllByPublishId(publishId);
        publishCommentRepository.deleteAllByPublishId(publishId);
        return STATUS.success;
    }

    @Override
    public STATUS likeIt(Long publishId, UserLogin user) {
        if (!publishRepository.exists(publishId))
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
    public STATUS commentIt(Long publishId, Long fatherId, UserLogin user, String text) {
        if (!publishRepository.exists(publishId))
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
