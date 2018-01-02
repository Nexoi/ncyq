package com.seeu.ywq.api.release.user;

import com.seeu.ywq.trend.model.Picture;
import com.seeu.ywq.user.service.UserPictureService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户相册", description = "查看用户个人的私密/公开相册")
@RestController
@RequestMapping("/api/v1/user/picture")
public class UserPictureApi {

    @Autowired
    private UserPictureService userPictureService;

    @ApiOperation(value = "查看自己私密相册【分页】")
    @GetMapping("/close")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listColse(@AuthenticationPrincipal UserLogin authUser,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(userPictureService.findAllMine(authUser.getUid(), Picture.ALBUM_TYPE.close, new PageRequest(page, size)));
    }

    @ApiOperation(value = "查看自己公开相册【分页】")
    @GetMapping("/open")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listOpen(@AuthenticationPrincipal UserLogin authUser,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(userPictureService.findAllMine(authUser.getUid(), Picture.ALBUM_TYPE.open, new PageRequest(page, size)));
    }
}
