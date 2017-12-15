package com.seeu.ywq.user.service.impl;

import com.seeu.ywq.user.dto.PictureVO;
import com.seeu.ywq.user.dto.PublishPictureVO;
import com.seeu.ywq.user.dto.PublishVO;
import com.seeu.ywq.user.dto.PublishVideoVO;
import com.seeu.ywq.user.model.Picture;
import com.seeu.ywq.user.model.Publish;
import com.seeu.ywq.user.repository.PublishRepository;
import com.seeu.ywq.user.service.PublishService;
import com.seeu.ywq.user.service.UserPictureService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PublishServiceImpl implements PublishService {
    @Resource
    private PublishRepository publishRepository;
    @Autowired
    private UserPictureService userPictureService;

    @Override
    public PublishVO transferToVO(Publish publish, Long uid) {
        if (publish == null) return null;

        switch (publish.getType()) {
            case picture:
                PublishPictureVO vop = new PublishPictureVO();
                BeanUtils.copyProperties(publish, vop);
                vop.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vop.setLikedUserList(publish.getLikedUserList() == null || publish.getLikedUserList().size() == 0 ? new ArrayList<>() : publish.getLikedUserList());
                vop.setCoverPictureUrl(publish.getPictures() == null || publish.getPictures().size() == 0 ? null : userPictureService.transferToVO(publish.getPictures().get(0), uid));
                vop.setPictures(userPictureService.transferToVO(publish.getPictures(), uid));
                return vop;
            case video:
                PublishVideoVO vod = new PublishVideoVO();
                BeanUtils.copyProperties(publish, vod);
                vod.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vod.setLikedUserList(publish.getLikedUserList() == null || publish.getLikedUserList().size() == 0 ? new ArrayList<>() : publish.getLikedUserList());
                // TODO video 权限得加
                return vod;
            case word:
            default:
                PublishVO vo = new PublishVO();
                BeanUtils.copyProperties(publish, vo);
                vo.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vo.setLikedUserList(publish.getLikedUserList() == null || publish.getLikedUserList().size() == 0 ? new ArrayList<>() : publish.getLikedUserList());
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
                PublishPictureVO vop = new PublishPictureVO();
                BeanUtils.copyProperties(publish, vop);
                vop.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vop.setLikedUserList(publish.getLikedUserList() == null || publish.getLikedUserList().size() == 0 ? new ArrayList<>() : publish.getLikedUserList());
                vop.setCoverPictureUrl(publish.getPictures() == null || publish.getPictures().size() == 0 ? null : userPictureService.transferToVO(publish.getPictures().get(0)));
                vop.setPictures(userPictureService.transferToVO(publish.getPictures()));
                return vop;
            case video:
                PublishVideoVO vod = new PublishVideoVO();
                BeanUtils.copyProperties(publish, vod);
                vod.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vod.setLikedUserList(publish.getLikedUserList() == null || publish.getLikedUserList().size() == 0 ? new ArrayList<>() : publish.getLikedUserList());
                // TODO video 权限得加
                return vod;
            case word:
            default:
                PublishVO vo = new PublishVO();
                BeanUtils.copyProperties(publish, vo);
                vo.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vo.setLikedUserList(publish.getLikedUserList() == null || publish.getLikedUserList().size() == 0 ? new ArrayList<>() : publish.getLikedUserList());
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

    @Override
    public Page findAllByPublishId(Long publishId, Long uid, Pageable pageable) {
        Page page = publishRepository.findAllByIdAndUid(publishId, uid, pageable);
        List<Publish> publishes = page.getContent();
        if (publishes == null || publishes.size() == 0)
            return page;
        List<PublishVO> vos = transferToVO(publishes, uid);
        return new PageImpl(vos, pageable, page.getTotalElements());
    }

    //【匿名】
    @Override
    public Page findAllByUid(Long uid, Pageable pageable) {
        Page page = publishRepository.findAllByUid(uid,pageable);
        List<Publish> publishes = page.getContent();
        if (publishes == null || publishes.size() == 0)
            return page;
        List<PublishVO> vos = transferToVO(publishes, uid);
        return new PageImpl(vos, pageable, page.getTotalElements());
    }

    //【匿名】
    @Override
    public PublishVO findOneByPublishId(Long publishId) {
        Publish publish = publishRepository.findOne(publishId);
        if (publish == null) return null;
        return transferToVO(publish);
    }

    @Override
    public PublishVO findOneByPublishId(Long publishId, Long uid) {
        Publish publish = publishRepository.findByIdAndUid(publishId, uid);
        if (publish == null) return null;
        //.. 如果为本人，则不会做权限数据库搜索
        return transferToVO(publish, uid);
    }

    ///////////////////////*** helper ***/////////////////////////////

    private List<Picture> cleanDeleteTags(List<Picture> pictures) {
        if (pictures == null) return new ArrayList<>();
        for (Picture picture : pictures) {
            if (picture == null) continue;
            picture.setDeleteTime(null);
            picture.setDeleteFlag(null);
        }
        return pictures;
    }

    private Picture cleanDeleteTags(Picture picture) {
        if (picture == null) return null;
        picture.setDeleteTime(null);
        picture.setDeleteFlag(null);
        return picture;
    }
}
