package com.seeu.ywq.trend.service;

import com.seeu.ywq.trend.dvo.PublishVO;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.model.PublishComment;
import com.seeu.ywq.trend.model.PublishLikedUser;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublishService {

    ///////////////////////////////////////////************** basic curd operations ***************////////////////////////////////////////////////////

    Publish findOne(Long publishId);

    Publish findOne(Long uid, Long publishId);

    Publish save(Publish publish);

    ///////////////////////////////////////////************** vo query operations ***************////////////////////////////////////////////////////

    Page findAllByUid(Long uid, boolean canVisitClosedResource, Pageable pageable);

    PublishVO findOneByPublishId(Long publishId, boolean canVisitClosedResource);

    ///////////////////////////////////////////************** transfer operations ***************////////////////////////////////////////////////////

    PublishVO transferToVO(Publish publish, boolean canVisitClosedResource);

    List<PublishVO> transferToVO(List<Publish> publishs, boolean canVisitClosedResource);

    ///////////////////////////////////////////************** other operations ***************////////////////////////////////////////////////////

    /* 基于 findOneByPublishId(publishId,uid) */
    PublishVO viewIt(Long publishId, Long uid);

    /* 基于 findOneByPublishId(publishId) */
    PublishVO viewIt(Long publishId);

    STATUS deletePublish(Long publishId);

    Page<PublishLikedUser> listLikedUser(Long publishId, Pageable pageable);

    STATUS likeIt(Long publishId, UserLogin user);

    STATUS dislikeIt(Long publishId, Long uid);

    PublishComment getComment(Long commentId);

    Page<PublishComment> listComments(Long publishId, Pageable pageable);

    STATUS commentIt(Long publishId, Long fatherId, UserLogin user, String text);

    STATUS deleteComment(Long commentId);

    public enum STATUS {
        existed_not_modify, // 存在，未做修改
        not_existed,         // 不存在，未做修改
        not_found_publish,  // 不存在该动态
        not_match,           // 不匹配
        success,
        failure
    }
}