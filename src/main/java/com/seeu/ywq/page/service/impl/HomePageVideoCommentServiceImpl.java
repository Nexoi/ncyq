package com.seeu.ywq.page.service.impl;

import com.seeu.ywq.page.model.HomePageVideoComment;
import com.seeu.ywq.page.repository.HomePageVideoCommentRepository;
import com.seeu.ywq.page.service.HomePageVideoCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HomePageVideoCommentServiceImpl implements HomePageVideoCommentService {

    @Resource
    private HomePageVideoCommentRepository repository;

    @Override
    public Page findAllByVideoId(Long videoId, Pageable pageable) {
        return repository.findAllByVideoIdAndDeleteFlagAndFatherIdIsNull(videoId, HomePageVideoComment.DELETE_FLAG.show, pageable);
    }

    @Override
    public HomePageVideoComment findOne(Long commentId) {
        return repository.findByVideoIdAndDeleteFlag(commentId, HomePageVideoComment.DELETE_FLAG.show);
    }

    @Override
    public HomePageVideoComment save(HomePageVideoComment comment) {
        return repository.save(comment);
    }
}
