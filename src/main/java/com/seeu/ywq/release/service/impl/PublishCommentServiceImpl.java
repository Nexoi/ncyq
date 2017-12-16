package com.seeu.ywq.release.service.impl;

import com.seeu.ywq.release.dvo.PublishCommentVO;
import com.seeu.ywq.release.model.PublishComment;
import com.seeu.ywq.release.service.PublishCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublishCommentServiceImpl implements PublishCommentService {
    @Override
    public PublishCommentVO transferToVO(PublishComment comment) {
        if (comment == null) return null;
        PublishCommentVO vo = new PublishCommentVO();
        BeanUtils.copyProperties(comment, vo);
        List<PublishComment> childComments = comment.getChildComments();
        vo.setChildComments(transferToVO(childComments));// 递归下去
        return vo;
    }

    @Override
    public List<PublishCommentVO> transferToVO(List<PublishComment> comments) {
        if (comments == null || comments.size() == 0) return new ArrayList<>();
        List<PublishCommentVO> vos = new ArrayList<>();
        for (PublishComment comment : comments) {
            vos.add(transferToVO(comment));
        }
        return vos;
    }
}
