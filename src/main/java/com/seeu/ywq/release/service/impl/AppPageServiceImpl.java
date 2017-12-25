package com.seeu.ywq.release.service.impl;

import com.seeu.third.qiniu.FileUploadService;
import com.seeu.ywq.release.dvo.apppage.HomePageVOUser;
import com.seeu.ywq.release.dvo.apppage.HomePageVOVideo;
import com.seeu.ywq.release.model.Image;
import com.seeu.ywq.release.model.Video;
import com.seeu.ywq.release.model.apppage.Advertisement;
import com.seeu.ywq.release.model.apppage.HomePageUser;
import com.seeu.ywq.release.model.apppage.HomePageUserPKeys;
import com.seeu.ywq.release.model.apppage.HomePageVideo;
import com.seeu.ywq.release.repository.ImageRepository;
import com.seeu.ywq.release.repository.VideoRepository;
import com.seeu.ywq.release.repository.apppage.HomePageVideoRepository;
import com.seeu.ywq.release.repository.apppage.PageAdvertisementRepository;
import com.seeu.ywq.release.repository.apppage.HomePageUserRepository;
import com.seeu.ywq.release.service.AppPageService;
import com.seeu.ywq.release.service.AppVOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AppPageServiceImpl implements AppPageService {

    @Resource
    private PageAdvertisementRepository pageAdvertisementRepository;
    @Resource
    private HomePageUserRepository homePageUserRepository;
    @Resource
    private HomePageVideoRepository homePageVideoRepository;
    @Autowired
    private AppVOService appVOService;
    @Autowired
    private FileUploadService fileUploadService;
    @Resource
    private VideoRepository videoRepository;
    @Resource
    private ImageRepository imageRepository; // 存储图片之用

    @Override
    public STATUS addUserConfigurer(HomePageUser.CATEGORY category, Long uid, Integer orderId) {
        if (homePageUserRepository.exists(new HomePageUserPKeys(category, uid)))
            return STATUS.exist;
        HomePageUser config = new HomePageUser();
        config.setOrderId(orderId);
        config.setCategory(category);
        config.setUid(uid);
        config.setCreateTime(new Date());
        homePageUserRepository.save(config);
        return STATUS.success;
    }

    @Override
    public STATUS deleteUserConfigurer(HomePageUser.CATEGORY category, Long uid) {
        if (!homePageUserRepository.exists(new HomePageUserPKeys(category, uid)))
            return STATUS.not_exist;
        homePageUserRepository.delete(new HomePageUserPKeys(category, uid));
        // TODO 如果是视频的话，可以尝试删除视频信息，清理部分内容
        return STATUS.success;
    }

    @Override
    public STATUS addAdvertisement(MultipartFile imageFile, Advertisement.CATEGORY category, String url, Integer orderId) {
        // 文件上传
        try {
            Image image = fileUploadService.uploadImage(imageFile);
            image = imageRepository.save(image); // 持久化（先持久化才能被 set 进去。。）
            // 持久化
            Advertisement advertisement = new Advertisement();
            advertisement.setImage(image);
            advertisement.setUrl(url);
            advertisement.setCategory(category);
            advertisement.setOrderId(orderId);
            advertisement.setCreateTime(new Date());
            pageAdvertisementRepository.save(advertisement);
            return STATUS.success;
        } catch (Exception e) {
            return STATUS.failure;
        }
    }

    @Override
    public STATUS deleteAdvertisement(Long advertisementId) {
        Advertisement advertisement = pageAdvertisementRepository.findOne(advertisementId);
        if (advertisement == null) return STATUS.not_exist;
        pageAdvertisementRepository.delete(advertisementId);
        return STATUS.success;
    }

    @Override
    public STATUS addVideo(MultipartFile videoFile, MultipartFile coverImage, Long uid, String title, HomePageVideo.CATEGORY category, Integer orderId) {
        try {
            // video
            Video video = fileUploadService.uploadVideo(videoFile);
            Image image = fileUploadService.uploadImage(coverImage);
            video.setCoverUrl(image.getImageUrl()); // 设定一张封面
            // cover
            Video savedVideo = videoRepository.save(video);
            Image savedImage = imageRepository.save(image);
            HomePageVideo pageVideo = new HomePageVideo();
            pageVideo.setCategory(category);
            pageVideo.setUid(uid);
            pageVideo.setTitle(title);
            pageVideo.setViewNum(0l);
            pageVideo.setDeleteFlag(HomePageVideo.DELETE_FLAG.show);
            pageVideo.setVideo(savedVideo);
            pageVideo.setCoverImage(savedImage);
            pageVideo.setCreateTime(new Date());
            homePageVideoRepository.save(pageVideo);
            return STATUS.success;
        } catch (IOException e) {
            return STATUS.failure;
        }
    }

    @Override
    public STATUS deleteVideo(Long videoId) {
        HomePageVideo video = homePageVideoRepository.findOne(videoId);
        if (video == null || video.getDeleteFlag() != HomePageVideo.DELETE_FLAG.show)
            return STATUS.not_exist;
        video.setDeleteFlag(HomePageVideo.DELETE_FLAG.delete);
        homePageVideoRepository.saveAndFlush(video);
        return STATUS.success;
    }

    @Override
    public List<Advertisement> getHomePage_Advertisements() {
        return pageAdvertisementRepository.findAllByCategory(Advertisement.CATEGORY.homepage);
    }

    @Override
    public List<Advertisement> getVideo_Advertisements() {
        return pageAdvertisementRepository.findAllByCategory(Advertisement.CATEGORY.videopage);
    }

    @Override
    public List<HomePageVOUser> getHomePage_NewHotsPerson() {
        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.HomePage_HotsPerson.ordinal());
        return appVOService.formUserVO(list);
    }

    @Override
    public List<HomePageVOUser> getHomePage_NewActors() {
        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.HomePage_Actor.ordinal());
        return appVOService.formUserVO(list);
    }

    @Override
    public List<HomePageVOUser> getYouWuPage_New() {
        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.YouWuPage_New.ordinal());
        return appVOService.formUserVO(list);
    }

    @Override
    public List<HomePageVOUser> getYouWuPage_Suggestion() {
        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.YouWuPage_Suggest.ordinal());
        return appVOService.formUserVO(list);
    }


    @Override
    public List<HomePageVOUser> getHotsPerson_New() {
        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.HotsPerson_New.ordinal());
        return appVOService.formUserVO(list);
    }

    @Override
    public List<HomePageVOUser> getHotsPerson_Suggestion() {
        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.HotsPerson_Suggest.ordinal());
        return appVOService.formUserVO(list);
    }

    @Override
    public List<HomePageVOVideo> getVideo_HD() {
        List list = homePageVideoRepository.findThemByCategory(HomePageVideo.CATEGORY.hd.ordinal());
        return appVOService.formVideoVO(list);
    }

    @Override
    public List<HomePageVOVideo> getVideo_VR() {
        List list = homePageVideoRepository.findThemByCategory(HomePageVideo.CATEGORY.vr.ordinal());
        return appVOService.formVideoVO(list);
    }


}
