package com.seeu.ywq.release.service.impl;

import com.seeu.ywq.release.dvo.PublishLikedUserVO;
import com.seeu.ywq.release.model.PublishLikedUser;
import com.seeu.ywq.release.service.PublishLikedUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublishLikedUserServiceImpl implements PublishLikedUserService {
    @Override
    public PublishLikedUserVO transferToVO(PublishLikedUser user) {
        if (user == null) return null;
        PublishLikedUserVO vo = new PublishLikedUserVO();
        vo.setHeadIconUrl(user.getHeadIconUrl());
        vo.setUid(user.getUid());
        return vo;
    }

    @Override
    public List<PublishLikedUserVO> transferToVO(List<PublishLikedUser> users) {
        if (users == null || users.size() == 0) return new ArrayList<>();
        List<PublishLikedUserVO> vos = new ArrayList<>();
        for (PublishLikedUser user : users) {
            vos.add(transferToVO(user));
        }
        return vos;
    }
}
