package com.seeu.ywq.api.release.page;

import com.seeu.ywq.page.model.HomePageCategory;
import com.seeu.ywq.page.service.AppHomePageService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "APP页面数据主页接口", description = "首页四栏")
@RestController
@RequestMapping("/api/v1/page")
public class HomePageApi {
    @Autowired
    private AppHomePageService appHomePageService;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private HomePageVideoService homePageVideoService;

    @ApiOperation(value = "首页")
    @GetMapping("/homepage")
    public ResponseEntity homePage(@AuthenticationPrincipal UserLogin authUser) {
        List<HomePageCategory> categoryList = null;
        if (authUser == null)
            categoryList = appHomePageService.queryAllByPage(HomePageCategory.PAGE.home);
        else
            categoryList = appHomePageService.queryAllByPage(authUser.getUid(), HomePageCategory.PAGE.home);
        return ResponseEntity.ok(categoryList);
//        Map map = new HashMap();
//        map.put("newHotPerson", appHomePageService.getHomePage_NewHotsPerson());
//        map.put("newActor", appHomePageService.getHomePage_NewActors());
//        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "首页广告")
    @GetMapping("/homepage/advertisements")
    public ResponseEntity homePageAdvertisements() {
        return ResponseEntity.ok(advertisementService.getAdvertisements(Advertisement.CATEGORY.HomePage));
    }

    @ApiOperation(value = "网红")
    @GetMapping("/hot-person")
    public ResponseEntity hotPerson(@AuthenticationPrincipal UserLogin authUser) {
        List<HomePageCategory> categoryList = null;
        if (authUser == null)
            categoryList = appHomePageService.queryAllByPage(HomePageCategory.PAGE.hotsperson);
        else
            categoryList = appHomePageService.queryAllByPage(authUser.getUid(), HomePageCategory.PAGE.hotsperson);
        return ResponseEntity.ok(categoryList);
//        Map map = new HashMap();
//        map.put("news", appHomePageService.getHotsPerson_New());
//        map.put("suggestion", appHomePageService.getHotsPerson_Suggestion());
//        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "尤物")
    @GetMapping("/youwu")
    public ResponseEntity youwu(@AuthenticationPrincipal UserLogin authUser) {
        List<HomePageCategory> categoryList = null;
        if (authUser == null)
            categoryList = appHomePageService.queryAllByPage(HomePageCategory.PAGE.youwu);
        else
            categoryList = appHomePageService.queryAllByPage(authUser.getUid(), HomePageCategory.PAGE.youwu);
        return ResponseEntity.ok(categoryList);
//        Map map = new HashMap();
//        map.put("news", appHomePageService.getYouWuPage_New());
//        map.put("suggestion", appHomePageService.getYouWuPage_Suggestion());
//        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "视频", notes = "字段：hd 表示“高清视频”；字段：vr 表示“VR视频”")
    @GetMapping("/video")
    public ResponseEntity video(@AuthenticationPrincipal UserLogin authUser) {
        Long visitorUid = null;
        if (authUser != null) visitorUid = authUser.getUid();
        Map map = new HashMap();
        map.put("hd", homePageVideoService.getVideo_HD(visitorUid));
        map.put("vr", homePageVideoService.getVideo_VR(visitorUid));
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "视频页广告")
    @GetMapping("/video/advertisements")
    public ResponseEntity videoAdvertisements() {
        return ResponseEntity.ok(advertisementService.getAdvertisements(Advertisement.CATEGORY.VideoPage));
    }
}
