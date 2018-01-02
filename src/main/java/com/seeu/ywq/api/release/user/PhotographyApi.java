package com.seeu.ywq.api.release.user;

import com.seeu.ywq.page.service.PhotographyService;
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

@Api(tags = "下载相册(摄影集)", description = "管理员通过接口进行上传照片，用户在此下载")
@RestController
@RequestMapping("/api/v1/photography")
public class PhotographyApi {
    @Autowired
    private PhotographyService photographyService;

    @ApiOperation(value = "查看自己照片下载页面【分页】", notes = "管理员通过接口进行上传照片，用户可以在此下载")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity listDownloads(@AuthenticationPrincipal UserLogin authUser,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(photographyService.findAllByUid(authUser.getUid(), new PageRequest(page, size)));
    }
}
