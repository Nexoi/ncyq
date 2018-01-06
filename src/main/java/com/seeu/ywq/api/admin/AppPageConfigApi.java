package com.seeu.ywq.api.admin;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceAddException;
import com.seeu.ywq.exception.ResourceAlreadyExistedException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page.model.Advertisement;
import com.seeu.ywq.page.model.HomePageUser;
import com.seeu.ywq.page.model.HomePageVideo;
import com.seeu.ywq.page.service.AppHomePageService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "APP 页面配置/广告配置", description = "APP 首页四个页面内容")
@RestController("adminAppPageConfigApi")
@RequestMapping("/api/admin/v1/app/page")
@PreAuthorize("hasRole('ADMIN')")
public class AppPageConfigApi {

    @Autowired
    private AppHomePageService appHomePageService;

    @ApiOperation(value = "首页、尤物、网红信息增添")
    @PostMapping("/person")
    public ResponseEntity addPerson(@AuthenticationPrincipal UserLogin authUser,
                                    HomePageUser.CATEGORY category,
                                    @RequestParam(required = true) Integer order,
                                    @RequestParam(required = false) Long uid) {
        if (order == null || order < 0)
            return ResponseEntity.status(400).body(R.code(4000).message("序号不能为空或负数").build());
        // add user
        try {
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
            HomePageVideo homePageVideo = appHomePageService.addVideo(video, coverImage, uid, title, category, order);
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
            appHomePageService.addAdvertisement(image, category, url, order);
            return ResponseEntity.status(201).body(R.code(201).message("添加成功！").build());
        } catch (ResourceAddException e) {
            return ResponseEntity.badRequest().body(R.code(400).message("添加失败").build());
        }
    }

    @ApiOperation(value = "删除该尤物、网红配置记录")
    @DeleteMapping("/person/{category}/{uid}")
    public ResponseEntity deletePerson(@PathVariable HomePageUser.CATEGORY category, @PathVariable Long uid) {
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
            appHomePageService.deleteVideo(videoId);
            return ResponseEntity.status(200).body(R.code(200).message("删除成功！").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该视频记录").build());
        }
    }

    @ApiOperation(value = "删除广告")
    @DeleteMapping("/advertisement/{advertisementId}")
    public ResponseEntity deleteAdvertisement(@PathVariable Long advertisementId) {
        try {
            appHomePageService.deleteAdvertisement(advertisementId);
            return ResponseEntity.status(200).body(R.code(200).message("删除成功！").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该广告记录").build());
        }
    }
}
