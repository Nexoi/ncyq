package com.seeu.ywq.page.service;

import com.seeu.ywq.page.model.HomePageVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomePageVideoService {
    // 自带浏览次数 +1
    HomePageVideo findOne(Long videoId);


    Page findAllByUid(Long uid, Pageable pageable);
}
