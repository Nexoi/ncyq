package com.seeu.ywq.release.service.apppage;

import com.seeu.ywq.release.model.apppage.HomePageVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomePageVideoService {
    // 自带浏览次数 +1
    HomePageVideo findOne(Long videoId);


    Page findAllByUid(Long uid, Pageable pageable);
}
