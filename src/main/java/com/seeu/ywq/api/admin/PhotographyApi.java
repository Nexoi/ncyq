package com.seeu.ywq.api.admin;

import com.seeu.core.R;
import com.seeu.ywq.release.model.apppage.Photography;
import com.seeu.ywq.release.service.apppage.PhotographyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(tags = "摄影集（下载作品）", description = "管理员根据用户 uid 上传摄影作品")
@RestController("adminPhotographyApi")
@RequestMapping("/api/admin/v1/photography")
@PreAuthorize("hasRole('ADMIN')")
public class PhotographyApi {
    @Autowired
    private PhotographyService photographyService;

    @ApiOperation(value = "上传摄影图片", notes = "上传摄影图集，并指定用户 uid，可以分批次多次上传，以 uid 为唯一标识")
    @PostMapping("/{uid}")
    public ResponseEntity postPhotographies(@PathVariable Long uid,
                                            MultipartFile[] images) {
        try {
            List<Photography> photographies = photographyService.save(uid, images);
            return ResponseEntity.ok(photographies);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(R.code(500).message("文件上传失败").build());
        }
    }
}
