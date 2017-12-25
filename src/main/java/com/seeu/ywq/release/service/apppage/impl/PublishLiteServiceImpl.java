package com.seeu.ywq.release.service.apppage.impl;

import com.seeu.ywq.release.dvo.apppage.PublishLiteVO;
import com.seeu.ywq.release.dvo.apppage.PublishLiteVOPicture;
import com.seeu.ywq.release.dvo.apppage.PublishLiteVOVideo;
import com.seeu.ywq.release.model.apppage.PublishLite;
import com.seeu.ywq.release.repository.apppage.PublishLiteRepository;
import com.seeu.ywq.release.service.ResourceAuthService;
import com.seeu.ywq.release.service.UserPictureService;
import com.seeu.ywq.release.service.apppage.AppVOService;
import com.seeu.ywq.release.service.apppage.PublishLiteService;
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
public class PublishLiteServiceImpl implements PublishLiteService {

    @Autowired
    private UserPictureService userPictureService;
    @Autowired
    private ResourceAuthService resourceAuthService;
    @Autowired
    private AppVOService appVOService;
    @Resource
    private PublishLiteRepository publishLiteRepository;

    @Override
    public Page<PublishLiteVO> findAllByTagIds(Long visitorUid, Pageable pageable, Long... ids) {
        List list = publishLiteRepository.queryItUseMyTags(Arrays.asList(ids), pageable);
        Integer totalSize = publishLiteRepository.countItUseMyTags(Arrays.asList(ids));
        list = appVOService.formPublishLite(list);
        List transferList = transferToVO(list, visitorUid);
        return new PageImpl<>(transferList, pageable, totalSize);
    }

    @Override
    public Page<PublishLiteVO> findAll(Long visitorUid, Pageable pageable) {
        return null;
    }

    @Override
    public Page<PublishLiteVO> findAllByFollowedUids(Long visitorUid, Pageable pageable, Long... uids) {
        List list = publishLiteRepository.queryItUseFollowedUids(Arrays.asList(uids), pageable);
        Integer totalSize = publishLiteRepository.countItUseFollowedUids(Arrays.asList(uids));
        list = appVOService.formPublishLite(list);
        List transferList = transferToVO(list, visitorUid);
        return new PageImpl<>(transferList, pageable, totalSize);
    }

    @Override
    public Page<PublishLiteVO> findAllByUid(Long uid, Pageable pageable) {
        Page page = publishLiteRepository.findAllByUid(uid, pageable);
        List<PublishLite> list = page.getContent();
        if (list.size() == 0) return page;
        List<PublishLiteVO> vos = new ArrayList<>();
        for (PublishLite publishLite : list) {
            PublishLiteVO vo = new PublishLiteVO();
            BeanUtils.copyProperties(publishLite, vo);
            vos.add(vo);
        }
        return new PageImpl<>(vos, pageable, page.getTotalElements());
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
            vos.add(transferToVO(publish, resourceAuthService.canVisit(visitorUid, publish.getId())));
        }
        return vos;
    }
}
