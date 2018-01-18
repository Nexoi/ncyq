package com.seeu.ywq.api.release.user;

import com.seeu.ywq.trend.service.PublishPictureService;
import com.seeu.ywq.user.service.UserPhotoWallService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户个人中心相册封面", description = "照片墙、动态封面")
@RestController
@RequestMapping("/api/v1/user/cover")
public class CoverListApi {

    @Autowired
    private UserPhotoWallService userPhotoWallService;
    @Autowired
    private PublishPictureService publishPictureService;

    @ApiOperation("获取三张封面")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getList(@AuthenticationPrincipal UserLogin authUser) {
        Long uid = authUser.getUid();
        Map map = new HashMap();
        map.put("photo_wall", userPhotoWallService.findCoverPhoto(uid));
        map.put("publish_open", publishPictureService.getCoverOpen(uid));
        map.put("publish_close", publishPictureService.getCoverClose(uid));
        return ResponseEntity.ok(map);
    }
}
