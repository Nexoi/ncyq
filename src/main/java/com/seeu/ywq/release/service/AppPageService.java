package com.seeu.ywq.release.service;

import com.seeu.ywq.release.dvo.apppage.HomePageVOVideo;
import com.seeu.ywq.release.dvo.apppage.HomePageVOUser;
import com.seeu.ywq.release.model.apppage.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * APP 首页搜索、列表
 */
public interface AppPageService {

    // 查看首页广告列表
    List<Advertisement> getHomePage_Advertisements();

    // 新晋网红
    List<HomePageVOUser> getHomePage_NewHotsPerson();

    // 新晋演员
    List<HomePageVOUser> getHomePage_NewActors();

    // 新晋尤物
    List<HomePageVOUser> getYouWuPage_New();

    // 推荐尤物
    Page getYouWuPage_Suggestion(Pageable pageable);

    // 新晋网红
    List<HomePageVOUser> get();

    // 推荐网红
    Page getHotsPerson_Suggestion(Pageable pageable);

    // 广告
    List<Advertisement> getVideo_Advertisements();

    // 高清视频
    Page<HomePageVOVideo> getVideo_HD(Pageable pageable);

    // VR 视频
    Page<HomePageVOVideo> getVideo_VR(Pageable pageable);
}
