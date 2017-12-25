package com.seeu.ywq.release.service.apppage.impl;

import com.seeu.ywq.release.model.apppage.HomePageVideoComment;
import com.seeu.ywq.release.repository.apppage.HomePageVideoCommentRepository;
import com.seeu.ywq.release.service.apppage.HomePageVideoCommentService;
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
