package com.seeu.ywq.api.release;

import com.seeu.ywq.release.service.apppage.AppHomePageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "APP页面数据主页接口", description = "首页四栏")
@RestController
@RequestMapping("/api/v1/page")
public class HomePageApi {
    @Autowired
    private AppHomePageService appHomePageService;

    @ApiOperation(value = "首页", notes = "字段：newHotPerson 表示“新晋网红”；字段：newActor 表示“新晋演员”")
    @GetMapping("/homepage")
    public ResponseEntity homePage() {
        Map map = new HashMap();
        map.put("newHotPerson", appHomePageService.getHomePage_NewHotsPerson());
        map.put("newActor", appHomePageService.getHomePage_NewActors());
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "首页广告")
    @GetMapping("/homepage/advertisements")
    public ResponseEntity homePageAdvertisements() {
        return ResponseEntity.ok(appHomePageService.getHomePage_Advertisements());
    }

    @ApiOperation(value = "网红", notes = "字段：news 表示“新晋”；字段：suggestion 表示“推荐”")
    @GetMapping("/hot-person")
    public ResponseEntity hotPerson() {
        Map map = new HashMap();
        map.put("news", appHomePageService.getHotsPerson_New());
        map.put("suggestion", appHomePageService.getHotsPerson_Suggestion());
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "尤物", notes = "字段：news 表示“新晋”；字段：suggestion 表示“推荐”")
    @GetMapping("/youwu")
    public ResponseEntity youwu() {
        Map map = new HashMap();
        map.put("news", appHomePageService.getYouWuPage_New());
        map.put("suggestion", appHomePageService.getYouWuPage_Suggestion());
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "视频", notes = "字段：hd 表示“高清视频”；字段：vr 表示“VR视频”")
    @GetMapping("/video")
    public ResponseEntity video() {
        Map map = new HashMap();
        map.put("hd", appHomePageService.getVideo_HD());
        map.put("vr", appHomePageService.getVideo_VR());
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "视频页广告")
    @GetMapping("/video/advertisements")
    public ResponseEntity videoAdvertisements() {
        return ResponseEntity.ok(appHomePageService.getVideo_Advertisements());
    }
}
