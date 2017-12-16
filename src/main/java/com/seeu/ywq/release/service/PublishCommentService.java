package com.seeu.ywq.release.service;

import com.seeu.ywq.release.dvo.PublishCommentVO;
import com.seeu.ywq.release.model.PublishComment;

import java.util.List;

public interface PublishCommentService {

    PublishCommentVO transferToVO(PublishComment comment);

    List<PublishCommentVO> transferToVO(List<PublishComment> comments);
}
