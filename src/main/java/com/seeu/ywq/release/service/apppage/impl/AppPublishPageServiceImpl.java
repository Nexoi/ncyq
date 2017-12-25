package com.seeu.ywq.release.service.apppage.impl;

import com.seeu.ywq.release.dvo.User$TagVO;
import com.seeu.ywq.release.dvo.apppage.PublishLiteVO;
import com.seeu.ywq.release.model.Fans;
import com.seeu.ywq.release.model.apppage.Advertisement;
import com.seeu.ywq.release.repository.apppage.PageAdvertisementRepository;
import com.seeu.ywq.release.service.FansService;
import com.seeu.ywq.release.service.TagService;
import com.seeu.ywq.release.service.apppage.AppPublishPageService;
import com.seeu.ywq.release.service.apppage.PublishLiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppPublishPageServiceImpl implements AppPublishPageService {
    @Resource
    private PageAdvertisementRepository pageAdvertisementRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private FansService fansService;
    @Autowired
    private PublishLiteService publishLiteService;

    @Override
    public List<Advertisement> getPublishPageAdvertisements() {
        return pageAdvertisementRepository.findAllByCategory(Advertisement.CATEGORY.PublishPage);
    }

    @Override
    public Page<PublishLiteVO> getTuijian(Long uid, Pageable pageable) {
        List<User$TagVO> myTags = tagService.findAllMine(uid);
        Long[] ids = new Long[myTags.size()];
        for (int i = 0; i < ids.length; i++) {
            User$TagVO tag = myTags.get(i);
            ids[i] = tag.getTagId();
        }
        Page page = publishLiteService.findAllByTagIds(uid, pageable, ids);
        return page;
    }

    @Override
    public Page<PublishLiteVO> getGuanzhu(Long uid, Pageable pageable) {
        List<Fans> myFolloweds = fansService.findAllByFansUid(uid);
        Long[] ids = new Long[myFolloweds.size()];
        for (int i = 0; i < ids.length; i++) {
            Fans var = myFolloweds.get(i);
            ids[i] = var.getFollowedUid();
        }
        Page page = publishLiteService.findAllByFollowedUids(uid, pageable, ids);
        return page;
    }

    @Override
    public Page<PublishLiteVO> getWhose(Long uid, Pageable pageable) {
        return publishLiteService.findAllByUid(uid, pageable);
    }
}
