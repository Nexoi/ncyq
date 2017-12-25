package com.seeu.ywq.release.service.apppage;

import com.seeu.ywq.release.model.apppage.HomePageVideoComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomePageVideoCommentService {

    Page findAllByVideoId(Long videoId, Pageable pageable);

    HomePageVideoComment findOne(Long commentId);

    HomePageVideoComment save(HomePageVideoComment comment);

}
