package com.seeu.ywq.page_video.service;

import com.seeu.ywq.page_video.model.HomePageVideoComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomePageVideoCommentService {

    Page findAllByVideoId(Long videoId, Pageable pageable);

    HomePageVideoComment findOne(Long commentId);

    HomePageVideoComment save(HomePageVideoComment comment);

}
