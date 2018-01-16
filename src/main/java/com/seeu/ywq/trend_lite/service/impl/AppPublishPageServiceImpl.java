package com.seeu.ywq.trend_lite.service.impl;

import com.seeu.ywq.trend_lite.dvo.PublishLiteVO;
import com.seeu.ywq.trend_lite.service.AppPublishPageService;
import com.seeu.ywq.trend_lite.service.PublishLiteService;
import com.seeu.ywq.user.dvo.TagVO;
import com.seeu.ywq.user.dvo.UserTagVO;
import com.seeu.ywq.user.model.Fans;
import com.seeu.ywq.user.service.FansService;
import com.seeu.ywq.user.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppPublishPageServiceImpl implements AppPublishPageService {
    @Autowired
    private TagService tagService;
    @Autowired
    private FansService fansService;
    @Autowired
    private PublishLiteService publishLiteService;


    @Override
    public Page<PublishLiteVO> getTuijian(Long uid, Pageable pageable) {
        Long[] ids = null;
        List<UserTagVO> myTags = tagService.findAllVO(uid);
        if (myTags.size() != 0) {
            // 如果用户有关注标签
            ids = new Long[myTags.size()];
            for (int i = 0; i < ids.length; i++) {
                UserTagVO tag = myTags.get(i);
                ids[i] = tag.getId();
            }
        } else {
            // 如果用户未关注任何标签
            List<TagVO> allTags = tagService.findAll();
            ids = new Long[allTags.size()];
            for (int i = 0; i < ids.length; i++) {
                TagVO vo = allTags.get(i);
                ids[i] = vo.getId();
            }
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
    public Page<PublishLiteVO> getWhose(Long visitorUid, Long uid, Pageable pageable) {
        return publishLiteService.findAllByUid(visitorUid, uid, pageable);
    }
}
