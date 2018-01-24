//package com.seeu.ywq.page.service.impl;
//
//import com.seeu.ywq.exception.ResourceAlreadyExistedException;
//import com.seeu.ywq.exception.ResourceNotFoundException;
//import com.seeu.ywq.page.dvo.HomePageVOUser;
//import com.seeu.ywq.page.model.HomePageCategory;
//import com.seeu.ywq.page.model.HomePageUser;
//import com.seeu.ywq.page.model.HomePageUserPKeys;
//import com.seeu.ywq.page.repository.HomePageUserRepository;
//import com.seeu.ywq.page.service.AppHomePageService;
//import com.seeu.ywq.page.service.HomePageCategoryService;
//import com.seeu.ywq.utils.AppVOUtils;
//import com.seeu.ywq.resource.model.Image;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class AppHomePageServiceImpl implements AppHomePageService {
//
//    @Resource
//    private HomePageUserRepository homePageUserRepository;
//    @Autowired
//    private HomePageCategoryService homePageCategoryService;
//    @Autowired
//    private AppVOUtils appVOUtils;
//
//    @Override
//    public void addUserConfigurer(Integer category, Long uid, Integer orderId) throws ResourceAlreadyExistedException {
//        if (homePageUserRepository.exists(new HomePageUserPKeys(category, uid)))
//            throw new ResourceAlreadyExistedException("Resource: [category:" + category + ",uid:" + uid + "] already exist.");
//        HomePageUser config = new HomePageUser();
//        config.setOrderId(orderId);
//        config.setCategory(category);
//        config.setUid(uid);
//        config.setCreateTime(new Date());
//        homePageUserRepository.save(config);
//    }
//
//    @Override
//    public void deleteUserConfigurer(Integer category, Long uid) throws ResourceNotFoundException {
//        if (!homePageUserRepository.exists(new HomePageUserPKeys(category, uid)))
//            throw new ResourceNotFoundException("Can not found Resource: [category:" + category + ",uid:" + uid + "]");
//        homePageUserRepository.delete(new HomePageUserPKeys(category, uid));
//        // TODO 如果是视频的话，可以尝试删除视频信息，清理部分内容
//        // ...
//    }
//
//
//    @Override
//    public List<HomePageCategory> queryAllByPage(Long visitorUid, HomePageCategory.PAGE page) {
//        List<HomePageCategory> categoryList = homePageCategoryService.findAllByPage(page);
//        if (categoryList == null || categoryList.size() == 0) return new ArrayList<>();
//        for (HomePageCategory category : categoryList) {
//            if (category == null) continue;
//            category.setData(this.getHomePageUsers(visitorUid, category.getCategory()));
//            category.setCategory(null); // 置空
//        }
//        return categoryList;
//    }
//
//    @Override
//    public List<HomePageCategory> queryAllByPage(HomePageCategory.PAGE page) {
//        return queryAllByPage(null, page);
//    }
//
//    @Override
//    public List<HomePageVOUser> getHomePageUsers(Long visitorUid, Integer category) {
//        if (category == null) return new ArrayList<>();
//        return formUserVO(visitorUid == null ? homePageUserRepository.findUserVOByCategory(category) : homePageUserRepository.findUserVOByCategory(visitorUid, category));
//    }
//
//    @Override
//    public List<HomePageVOUser> getHomePageUsers(Integer category) {
//        return getHomePageUsers(null, category);
//    }
//
//
//    private HomePageVOUser formUserVO(Object[] objects) {
//        if (objects == null || objects.length != 13 && objects.length != 14) return null;// 长度必须是 13 或 14 个
//        HomePageVOUser vo = new HomePageVOUser();
//        vo.setUid(appVOUtils.parseLong(objects[0]));
//        vo.setNickname(appVOUtils.parseString(objects[1]));
//        vo.setLikeNum(appVOUtils.parseLong(objects[2]));
//        vo.setHeadIconUrl(appVOUtils.parseString(objects[3]));
//        vo.setIdentifications(appVOUtils.parseBytesToLongList(objects[4]));
//        Image image = new Image();
//        image.setId(appVOUtils.parseLong(objects[5]));
//        image.setHeight(appVOUtils.parseInt(objects[6]));
//        image.setWidth(appVOUtils.parseInt(objects[7]));
//        image.setImageUrl(appVOUtils.parseString(objects[8]));
//        image.setThumbImage100pxUrl(appVOUtils.parseString(objects[9]));
//        image.setThumbImage200pxUrl(appVOUtils.parseString(objects[10]));
//        image.setThumbImage300pxUrl(appVOUtils.parseString(objects[11]));
//        image.setThumbImage500pxUrl(appVOUtils.parseString(objects[12]));
//        if (objects.length > 13)
//            vo.setLikeIt(1 == appVOUtils.parseInt(objects[13]));
//        vo.setCoverImage(image);
//        return vo;
//    }
//
//    private List<HomePageVOUser> formUserVO(List<Object[]> objects) {
//        if (objects == null || objects.size() == 0) return new ArrayList<>();
//        List<HomePageVOUser> vos = new ArrayList<>();
//        for (Object[] object : objects) {
//            vos.add(formUserVO(object));
//        }
//        return vos;
//    }
//
//}
