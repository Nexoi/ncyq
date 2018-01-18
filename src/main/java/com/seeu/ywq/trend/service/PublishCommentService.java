package com.seeu.ywq.trend.service;

import com.seeu.ywq.trend.dvo.PublishCommentVO;
import com.seeu.ywq.trend.model.PublishComment;

import java.util.List;

public interface PublishCommentService {

    PublishCommentVO transferToVO(PublishComment comment);

    List<PublishCommentVO> transferToVO(List<PublishComment> comments);
}
