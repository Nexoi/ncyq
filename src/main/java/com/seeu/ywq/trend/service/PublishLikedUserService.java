package com.seeu.ywq.trend.service;

import com.seeu.ywq.trend.dvo.PublishLikedUserVO;
import com.seeu.ywq.trend.model.PublishLikedUser;

import java.util.List;

public interface PublishLikedUserService {

    PublishLikedUserVO transferToVO(PublishLikedUser user);

    List<PublishLikedUserVO> transferToVO(List<PublishLikedUser> users);
}
