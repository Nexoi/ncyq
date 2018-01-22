package com.seeu.ywq.api.admin.configurer;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceAddException;
import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page.model.HomePageCategory;
import com.seeu.ywq.page.service.AppHomePageService;
import com.seeu.ywq.page.service.HomePageCategoryService;
import com.seeu.ywq.page_advertisement.model.Advertisement;
import com.seeu.ywq.page_advertisement.service.AdvertisementService;
import com.seeu.ywq.page_video.model.HomePageVideo;
import com.seeu.ywq.page_video.service.HomePageVideoService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "配置-APP 页面配置/广告配置", description = "APP 首页四个页面内容")
@RestController("adminAppPageConfigApi")
@RequestMapping("/api/admin/v1/app/page")
//@PreAuthorize("hasRole('ADMIN')")
public class AppPageConfigApi {

    @Autowired
    private AppHomePageService appHomePageService;
    @Autowired
    private HomePageCategoryService homePageCategoryService;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private HomePageVideoService homePageVideoService;


    @ApiOperation(value = "查看所有分类信息", notes = "首页、网红、尤物页面下的分类信息")
    @GetMapping("/categories")
    public ResponseEntity listCategory(@AuthenticationPrincipal UserLogin authUser) {
        return ResponseEntity.ok(homePageCategoryService.findAll());
    }

    @ApiOperation(value = "添加分类信息")
    @PostMapping("/{page}/categories")
    public ResponseEntity addCategory(@AuthenticationPrincipal UserLogin authUser,
                                      @PathVariable HomePageCategory.PAGE page,
                                      String title) {
        // 新建分类
        HomePageCategory categoryNew = new HomePageCategory();
        categoryNew.setPage(page);
        categoryNew.setPageName(page.name());
        categoryNew.setCategoryTitle(title);
        return ResponseEntity.ok(homePageCategoryService.save(categoryNew));
    }

    @ApiOperation(value = "修改分类信息")
    @PutMapping("/{page}/categories/{category}")
    public ResponseEntity updateCategory(@AuthenticationPrincipal UserLogin authUser,
                                         @PathVariable HomePageCategory.PAGE page,
                                         @PathVariable Integer category,
                                         String title) {
        // 先找到 page
        HomePageCategory categoryModel = homePageCategoryService.findById(page, category);
        if (categoryModel == null)
            return ResponseEntity.badRequest().body(R.code(4001).message("无此页面，不可修改"));
        // 新建分类
        categoryModel.setCategoryTitle(title);
        return ResponseEntity.ok(homePageCategoryService.save(categoryModel));
    }

    @ApiOperation(value = "删除分类信息")
    @DeleteMapping("/{page}/categories/{category}")
    public ResponseEntity deleteCategory(@AuthenticationPrincipal UserLogin authUser,
                                         @PathVariable HomePageCategory.PAGE page,
                                         @PathVariable Integer category) {
        // 先找到 page
        HomePageCategory categoryModel = homePageCategoryService.findFirstByPage(page);
        if (categoryModel == null)
            return ResponseEntity.ok().body(R.code(200).message("删除成功"));
        homePageCategoryService.delete(page, category);
        return ResponseEntity.ok().body(R.code(200).message("删除成功"));
    }

    @ApiOperation(value = "首页、尤物、网红信息增添")
    @PostMapping("/person/{page}/{category}")
    public ResponseEntity addPerson(@AuthenticationPrincipal UserLogin authUser,
                                    @PathVariable HomePageCategory.PAGE page,
                                    @PathVariable Integer category,
                                    @RequestParam(required = true) Integer order,
                                    @RequestParam(required = false) Long uid) {
        if (order == null || order < 0)
            return ResponseEntity.status(400).body(R.code(4000).message("序号不能为空或负数").build());
        // add user
        try {
            // 查看是否有该分类
            if (null == homePageCategoryService.findById(page, category))
                return ResponseEntity.badRequest().body(R.code(4001).message("无此分类信息，请先添加分类").build());
            appHomePageService.addUserConfigurer(category, uid, order);
            return ResponseEntity.status(201).body(R.code(201).message("添加成功！").build());
        } catch (ResourceAlreadyExistedException e) {
            return ResponseEntity.badRequest().body(R.code(4002).message("添加失败，请勿重复添加").build());
        }
    }

    @ApiOperation(value = "视频信息增添")
    @PostMapping("/video")
    public ResponseEntity addVideo(@AuthenticationPrincipal UserLogin authUser,
                                   HomePageVideo.CATEGORY category,
                                   @RequestParam(required = true) Integer order,
                                   @RequestParam(required = true) Long uid,
                                   String title,
                                   @RequestParam(required = false) MultipartFile video,
                                   @RequestParam(required = false) MultipartFile coverImage) {
        if (order == null || order < 0 || uid < 0)
            return ResponseEntity.status(400).body(R.code(4000).message("序号或用户 UID 不能为空或负数").build());
        // upload video
        try {
            HomePageVideo homePageVideo = homePageVideoService.addVideo(video, coverImage, uid, title, category, order);
            return ResponseEntity.status(200).body(homePageVideo);
        } catch (ResourceAddException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("添加失败【" + e.getMessage() + "】").build());
        }
    }

    @ApiOperation(value = "广告添加")
    @PostMapping("/advertisement")
    public ResponseEntity addAdvertisement(@AuthenticationPrincipal UserLogin authUser,
                                           Advertisement.CATEGORY category,
                                           MultipartFile image,
                                           String url,
                                           @RequestParam(required = true) Integer order) {
        if (order == null || order < 0)
            return ResponseEntity.status(400).body(R.code(4000).message("序号不能为空或负数").build());
        try {
            advertisementService.addAdvertisement(image, category, url, order);
            return ResponseEntity.status(201).body(R.code(201).message("添加成功！").build());
        } catch (ResourceAddException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("添加失败").build());
        }
    }

    @ApiOperation(value = "删除该尤物、网红配置记录")
    @DeleteMapping("/person/{category}/{uid}")
    public ResponseEntity deletePerson(@PathVariable Integer category, @PathVariable Long uid) {
        try {
            appHomePageService.deleteUserConfigurer(category, uid);
            return ResponseEntity.status(200).body(R.code(200).message("删除成功！").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("删除失败，找不到该配置记录").build());
        }
    }

    @ApiOperation(value = "删除视频记录")
    @DeleteMapping("/video/{videoId}")
    public ResponseEntity deleteVideo(@PathVariable Long videoId) {
        // set deleteFlag 即可
        try {
            homePageVideoService.deleteVideo(videoId);
            return ResponseEntity.status(200).body(R.code(200).message("删除成功！").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该视频记录").build());
        }
    }

    @ApiOperation(value = "删除广告")
    @DeleteMapping("/advertisement/{advertisementId}")
    public ResponseEntity deleteAdvertisement(@PathVariable Long advertisementId) {
        try {
            advertisementService.deleteAdvertisement(advertisementId);
            return ResponseEntity.status(200).body(R.code(200).message("删除成功！").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该广告记录").build());
        }
    }
}
