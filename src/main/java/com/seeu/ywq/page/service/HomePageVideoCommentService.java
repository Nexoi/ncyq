package com.seeu.ywq.page.service;

import com.seeu.ywq.page.model.HomePageVideoComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomePageVideoCommentService {

    Page findAllByVideoId(Long videoId, Pageable pageable);

    HomePageVideoComment findOne(Long commentId);

    HomePageVideoComment save(HomePageVideoComment comment);

}
