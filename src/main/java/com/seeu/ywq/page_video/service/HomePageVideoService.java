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

    Page findAllByUid(Long uid, Pageable pageable);

    HomePageVideo addVideo(MultipartFile videoFile, MultipartFile coverImage, Long uid, String title, HomePageVideo.CATEGORY category, Integer order) throws ResourceAddException;

    void deleteVideo(Long videoId) throws ResourceNotFoundException;

    // 高清视频
    List<HomePageVOVideo> getVideo_HD();

    // 高清视频
    List<HomePageVOVideo> getVideo_HD(Long visitorUid);

    // VR 视频
    List<HomePageVOVideo> getVideo_VR();

    // VR 视频
    List<HomePageVOVideo> getVideo_VR(Long visitorUid);
}
