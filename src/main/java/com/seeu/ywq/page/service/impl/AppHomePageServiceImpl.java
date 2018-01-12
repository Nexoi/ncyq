package com.seeu.ywq.page.service.impl;

import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.exception.ResourceAddException;
import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page.dvo.HomePageVOUser;
import com.seeu.ywq.page.dvo.HomePageVOVideo;
import com.seeu.ywq.page.dvo.SimpleUserVO;
import com.seeu.ywq.page.model.*;
import com.seeu.ywq.page.repository.HomePageUserRepository;
import com.seeu.ywq.page.repository.HomePageVideoRepository;
import com.seeu.ywq.page.repository.PageAdvertisementRepository;
import com.seeu.ywq.page.service.AppHomePageService;
import com.seeu.ywq.page.service.AppVOService;
import com.seeu.ywq.page.service.HomePageCategoryService;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.resource.repository.ImageRepository;
import com.seeu.ywq.resource.repository.VideoRepository;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class AppHomePageServiceImpl implements AppHomePageService {

    @Resource
    private PageAdvertisementRepository pageAdvertisementRepository;
    @Resource
    private HomePageUserRepository homePageUserRepository;
    @Resource
    private HomePageVideoRepository homePageVideoRepository;
    @Autowired
    private HomePageCategoryService homePageCategoryService;
    @Autowired
    private AppVOService appVOService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private UserReactService userReactService;
    @Resource
    private VideoRepository videoRepository;
    @Resource
    private ImageRepository imageRepository; // 存储图片之用

    @Override
    public void addUserConfigurer(Integer category, Long uid, Integer orderId) throws ResourceAlreadyExistedException {
        if (homePageUserRepository.exists(new HomePageUserPKeys(category, uid)))
            throw new ResourceAlreadyExistedException("Resource: [category:" + category + ",uid:" + uid + "] already exist.");
        HomePageUser config = new HomePageUser();
        config.setOrderId(orderId);
        config.setCategory(category);
        config.setUid(uid);
        config.setCreateTime(new Date());
        homePageUserRepository.save(config);
    }

    @Override
    public void deleteUserConfigurer(Integer category, Long uid) throws ResourceNotFoundException {
        if (!homePageUserRepository.exists(new HomePageUserPKeys(category, uid)))
            throw new ResourceNotFoundException("Can not found Resource: [category:" + category + ",uid:" + uid + "]");
        homePageUserRepository.delete(new HomePageUserPKeys(category, uid));
        // TODO 如果是视频的话，可以尝试删除视频信息，清理部分内容
        // ...
    }

    @Override
    public void addAdvertisement(MultipartFile imageFile, Advertisement.CATEGORY category, String url, Integer orderId) throws ResourceAddException {
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
        } catch (Exception e) {
            throw new ResourceAddException(e.getMessage());
        }
    }

    @Override
    public void deleteAdvertisement(Long advertisementId) throws ResourceNotFoundException {
        Advertisement advertisement = pageAdvertisementRepository.findOne(advertisementId);
        if (advertisement == null)
            throw new ResourceNotFoundException("Can not found Advertisement[ID:" + advertisementId + "]");
        pageAdvertisementRepository.delete(advertisementId);
    }

    @Override
    public HomePageVideo addVideo(MultipartFile videoFile, MultipartFile coverImage, Long uid, String title, HomePageVideo.CATEGORY category, Integer orderId) throws ResourceAddException {
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
            return homePageVideoRepository.save(pageVideo);
        } catch (IOException e) {
            throw new ResourceAddException("Resource add exception: 视频上传失败！");
        }
    }

    @Override
    public void deleteVideo(Long videoId) throws ResourceNotFoundException {
        HomePageVideo video = homePageVideoRepository.findOne(videoId);
        if (video == null || video.getDeleteFlag() != HomePageVideo.DELETE_FLAG.show)
            throw new ResourceNotFoundException("Can not found Resource[Video:" + videoId + "]");
        video.setDeleteFlag(HomePageVideo.DELETE_FLAG.delete);
        homePageVideoRepository.saveAndFlush(video);
    }

    @Override
    public List<Advertisement> getHomePage_Advertisements() {
        return pageAdvertisementRepository.findAllByCategory(Advertisement.CATEGORY.HomePage);
    }


    @Override
    public List<Advertisement> getVideo_Advertisements() {
        return pageAdvertisementRepository.findAllByCategory(Advertisement.CATEGORY.VideoPage);
    }

    @Override
    public List<HomePageCategory> queryAllByPage(Long visitorUid, HomePageCategory.PAGE page) {
        List<HomePageCategory> categoryList = homePageCategoryService.findAllByPage(page);
        if (categoryList == null || categoryList.size() == 0) return new ArrayList<>();
        for (HomePageCategory category : categoryList) {
            if (category == null) continue;
            category.setData(this.getHomePageUsers(visitorUid, category.getCategory()));
            category.setCategory(null); // 置空
        }
        return categoryList;
    }

    @Override
    public List<HomePageCategory> queryAllByPage(HomePageCategory.PAGE page) {
        return queryAllByPage(null, page);
    }

    @Override
    public List<HomePageVOUser> getHomePageUsers(Long visitorUid, Integer category) {
        if (category == null) return new ArrayList<>();
        return appVOService.formUserVO(visitorUid == null ? homePageUserRepository.findUserVOByCategory(category) : homePageUserRepository.findUserVOByCategory(visitorUid, category));
    }

    @Override
    public List<HomePageVOUser> getHomePageUsers(Integer category) {
        return getHomePageUsers(null, category);
    }

    //    @Override
//    public List<HomePageVOUser> getHomePage_NewHotsPerson() {
//        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.HomePage_HotsPerson.ordinal());
//        return appVOService.formUserVO(list);
//    }
//
//    @Override
//    public List<HomePageVOUser> getHomePage_NewActors() {
//        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.HomePage_Actor.ordinal());
//        return appVOService.formUserVO(list);
//    }
//
//    @Override
//    public List<HomePageVOUser> getYouWuPage_New() {
//        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.YouWuPage_New.ordinal());
//        return appVOService.formUserVO(list);
//    }
//
//    @Override
//    public List<HomePageVOUser> getYouWuPage_Suggestion() {
//        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.YouWuPage_Suggest.ordinal());
//        return appVOService.formUserVO(list);
//    }
//
//
//    @Override
//    public List<HomePageVOUser> getHotsPerson_New() {
//        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.HotsPerson_New.ordinal());
//        return appVOService.formUserVO(list);
//    }
//
//    @Override
//    public List<HomePageVOUser> getHotsPerson_Suggestion() {
//        List list = homePageUserRepository.findUserVOByCategory(HomePageUser.CATEGORY.HotsPerson_Suggest.ordinal());
//        return appVOService.formUserVO(list);
//    }
    @Override
    public List<HomePageVOVideo> getVideo_HD() {
        return getVideo_HD(null);
    }

    @Override
    public List<HomePageVOVideo> getVideo_VR() {
        return getVideo_VR(null);
    }

    @Override
    public List<HomePageVOVideo> getVideo_HD(Long visitorUid) {
        return formVOs(visitorUid, HomePageVideo.CATEGORY.hd.ordinal());
    }

    @Override
    public List<HomePageVOVideo> getVideo_VR(Long visitorUid) {
        return formVOs(visitorUid, HomePageVideo.CATEGORY.vr.ordinal());
    }

    private List<HomePageVOVideo> formVOs(Long visitorUid, Integer category) {
        List list = homePageVideoRepository.findThemByCategory(category);
        List<HomePageVOVideo> voVideos = appVOService.formVideoVO(list);
        if (voVideos == null || voVideos.size() == 0) return new ArrayList<>();
        List<Long> uids = new ArrayList<>();
        for (HomePageVOVideo voVideo : voVideos) {
            Long uid = voVideo.getUid();
            if (uid != null) uids.add(uid);
        }
        // 查询用户信息
        List<SimpleUserVO> users = userReactService.findAllSimpleUsers(visitorUid, uids);
        if (users == null || users.size() == 0) return voVideos;
        Map<Long, SimpleUserVO> map = new HashMap<>();
        for (SimpleUserVO vo : users) {
            map.put(vo.getUid(), vo);
        }
        // 装载信息
        for (HomePageVOVideo voVideo : voVideos) {
            voVideo.setUser(map.get(voVideo.getUid()));
            voVideo.setUid(null);// 清除不必要的信息
        }
        return voVideos;
    }

}
