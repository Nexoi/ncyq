package com.seeu.ywq.release.service;

import com.seeu.ywq.release.dvo.PublishLikedUserVO;
import com.seeu.ywq.release.model.PublishLikedUser;

import java.util.List;

public interface PublishLikedUserService {

    PublishLikedUserVO transferToVO(PublishLikedUser user);

    List<PublishLikedUserVO> transferToVO(List<PublishLikedUser> users);
}
