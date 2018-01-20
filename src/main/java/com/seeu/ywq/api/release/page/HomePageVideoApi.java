package com.seeu.ywq.api.release.page;

import com.seeu.ywq.page_advertisement.model.Advertisement;
import com.seeu.ywq.page_advertisement.service.AdvertisementService;
import com.seeu.ywq.page_video.service.HomePageVideoService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "APP页面数据视频接口", description = "视频列表")
@RestController
@RequestMapping("/api/v1/page")
public class HomePageVideoApi {
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private HomePageVideoService homePageVideoService;


//    @ApiOperation(value = "视频", notes = "字段：hd 表示“高清视频”；字段：vr 表示“VR视频”")
//    @GetMapping("/video")
//    public ResponseEntity video(@AuthenticationPrincipal UserLogin authUser) {
//        Long visitorUid = null;
//        if (authUser != null) visitorUid = authUser.getUid();
//        Map map = new HashMap();
//        map.put("hd", homePageVideoService.getVideo_HD(visitorUid));
//        map.put("vr", homePageVideoService.getVideo_VR(visitorUid));
//        return ResponseEntity.ok(map);
//    }

    @ApiOperation(value = "视频（VR）")
    @GetMapping("/video/vr")
    public ResponseEntity videoVR(@AuthenticationPrincipal UserLogin authUser,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size) {
        Long visitorUid = null;
        if (authUser != null) visitorUid = authUser.getUid();
        return ResponseEntity.ok(homePageVideoService.getVideo_VR(visitorUid, new PageRequest(page, size)));
    }

    @ApiOperation(value = "视频（高清）")
    @GetMapping("/video/hd")
    public ResponseEntity videoHD(@AuthenticationPrincipal UserLogin authUser,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size) {
        Long visitorUid = null;
        if (authUser != null) visitorUid = authUser.getUid();
        return ResponseEntity.ok(homePageVideoService.getVideo_HD(visitorUid, new PageRequest(page, size)));
    }

    @ApiOperation(value = "视频页广告")
    @GetMapping("/video/advertisements")
    public ResponseEntity videoAdvertisements() {
        return ResponseEntity.ok(advertisementService.getAdvertisements(Advertisement.CATEGORY.VideoPage));
    }
}
