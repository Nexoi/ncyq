package com.seeu.ywq.page.service.impl;

import com.seeu.ywq.page.dvo.PublishLiteVO;
import com.seeu.ywq.page.dvo.PublishLiteVOPicture;
import com.seeu.ywq.page.dvo.PublishLiteVOVideo;
import com.seeu.ywq.page.dvo.SimpleUserVO;
import com.seeu.ywq.trend.model.Picture;
import com.seeu.ywq.page.model.PublishLite;
import com.seeu.ywq.page.repository.PublishLiteRepository;
import com.seeu.ywq.resource.service.ResourceAuthService;
import com.seeu.ywq.user.service.UserPictureService;
import com.seeu.ywq.page.service.AppVOService;
import com.seeu.ywq.page.service.PublishLiteService;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class PublishLiteServiceImpl implements PublishLiteService {

    @Autowired
    private UserPictureService userPictureService;
    @Autowired
    private ResourceAuthService resourceAuthService;
    @Autowired
    private AppVOService appVOService;
    @Resource
    private PublishLiteRepository publishLiteRepository;
    @Autowired
    private UserReactService userReactService;

    @Override
    public Page<PublishLiteVO> findAllByTagIds(Long visitorUid, Pageable pageable, Long... ids) {
        if (ids == null || ids.length == 0) return new PageImpl<>(new ArrayList<>());
        List list = publishLiteRepository.queryItUseMyTags(visitorUid, Arrays.asList(ids), pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer totalSize = publishLiteRepository.countItUseMyTags(Arrays.asList(ids));
        list = appVOService.formPublishLite(list);
        completePicturesAndSimpleUsers(visitorUid, list); // 加载图片
        List transferList = transferToVO(list, visitorUid);
        return new PageImpl<>(transferList, pageable, totalSize);
    }


    @Override
    public Page<PublishLiteVO> findAllByFollowedUids(Long visitorUid, Pageable pageable, Long... uids) {
        if (uids == null || uids.length == 0) return new PageImpl<>(new ArrayList<>());
        List list = publishLiteRepository.queryItUseFollowedUids(visitorUid, Arrays.asList(uids), pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer totalSize = publishLiteRepository.countItUseFollowedUids(Arrays.asList(uids));
        list = appVOService.formPublishLite(list);
        completePicturesAndSimpleUsers(visitorUid, list); // 加载图片
        List transferList = transferToVO(list, visitorUid);
        return new PageImpl<>(transferList, pageable, totalSize);
    }

    @Override
    public Page<PublishLiteVO> findAllByUid(Long visitorUid, Long uid, Pageable pageable) {
        Page page = publishLiteRepository.findAllByUid(uid, pageable);
        List<PublishLite> list = page.getContent();
        // TODO
        completePicturesAndSimpleUsers(visitorUid, list); // 加载图片
        if (list.size() == 0) return page;
        List transferList = transferToVO(list, uid);
        return new PageImpl<>(transferList, pageable, page.getTotalElements());
    }

    // 将图片加载进 vo
    private void completePicturesAndSimpleUsers(Long visitorUid, List<PublishLite> vos) {
        if (vos == null || vos.size() == 0) return;
        // hash
        HashMap<Long, PublishLite> map = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (PublishLite vo : vos) {
            vo.setPictures(new ArrayList<>()); // 清空原有图片数据
            ids.add(vo.getId());
            userIds.add(vo.getUid());
            map.put(vo.getId(), vo);
        }
        // Pictures
        List<Picture> pictures = userPictureService.findAllByPublishIds(ids);
        if (pictures == null || pictures.size() == 0)
            return;
        for (Picture picture : pictures) {
            if (picture == null) continue;
            List pictureList = map.get(picture.getPublishId()).getPictures();
            if (pictureList == null) map.get(picture.getPublishId()).setPictures(pictureList = new ArrayList<>());
            pictureList.add(picture);
        }
        // Users
        List<SimpleUserVO> simpleUsers = userReactService.findAllSimpleUsers(visitorUid, userIds);
        HashMap<Long, SimpleUserVO> userMap = new HashMap();
        for (SimpleUserVO vo : simpleUsers) {
            userMap.put(vo.getUid(), vo);
        }
        for (PublishLite vo : vos) {
            vo.setUser(userMap.get(vo.getUid()));
        }
    }

    private PublishLiteVO transferToVO(PublishLite publish, boolean canVisitClosedResource) {
        if (publish == null) return null;
        switch (publish.getType()) {
            case picture:
                PublishLiteVOPicture vop = new PublishLiteVOPicture();
                BeanUtils.copyProperties(publish, vop);
                vop.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vop.setCoverPictureUrl(publish.getPictures() == null || publish.getPictures().size() == 0 ? null : userPictureService.transferToVO(publish.getPictures().get(0), canVisitClosedResource));
                vop.setPictures(userPictureService.transferToVO(publish.getPictures(), canVisitClosedResource));
                return vop;
            case video:
                PublishLiteVOVideo vod = new PublishLiteVOVideo();
                BeanUtils.copyProperties(publish, vod);
                vod.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                // TODO video 权限得加
                return vod;
            case word:
            default:
                PublishLiteVO vo = new PublishLiteVO();
                BeanUtils.copyProperties(publish, vo);
                vo.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                return vo;
        }
    }

    private List<PublishLiteVO> transferToVO(List<PublishLite> publishs, Long visitorUid) {
        List<PublishLiteVO> vos = new ArrayList<>();
        for (PublishLite publish : publishs) {
            if (publish == null) continue;
            boolean canVisit = visitorUid == publish.getUid() || resourceAuthService.canVisit(visitorUid, publish.getId());
            vos.add(transferToVO(publish, canVisit));
        }
        return vos;
    }
}
