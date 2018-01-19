package com.seeu.ywq.page_video.service;

import com.seeu.ywq.exception.ResourceAddException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page.dvo.HomePageVOVideo;
import com.seeu.ywq.page_video.model.HomePageVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HomePageVideoService {
    // 自带浏览次数 +1
    HomePageVideo findOne(Long videoId);

    HomePageVideo findOne(Long visitorUid, Long videoId);

    Page findAllByUid(Long uid, Pageable pageable);

    Page findAllByUid(Long visitorUid, Long uid, Pageable pageable);

    HomePageVideo addVideo(MultipartFile videoFile, MultipartFile coverImage, Long uid, String title, HomePageVideo.CATEGORY category, Integer order) throws ResourceAddException;

    HomePageVideo save(HomePageVideo video);

    void deleteVideo(Long videoId) throws ResourceNotFoundException;

    // 用於展示列表
    Page<HomePageVideo> findAllByCategory(HomePageVideo.CATEGORY category, Pageable pageable);

    // 用於展示列表
    Page<HomePageVideo> findAllByCategory(Long visitorUid, HomePageVideo.CATEGORY category, Pageable pageable);

    // 用于 vo 展示列表
    // 高清视频
    Page<HomePageVOVideo> getVideo_HD(Pageable pageable);

    // 高清视频
    Page<HomePageVOVideo> getVideo_HD(Long visitorUid, Pageable pageable);

    // VR 视频
    Page<HomePageVOVideo> getVideo_VR(Pageable pageable);

    // VR 视频
    Page<HomePageVOVideo> getVideo_VR(Long visitorUid, Pageable pageable);
}
