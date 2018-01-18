package com.seeu.ywq.api.admin;

import com.seeu.core.R;
import com.seeu.ywq.photography.model.Photography;
import com.seeu.ywq.photography.model.PhotographyDay;
import com.seeu.ywq.photography.service.PhotographyDayService;
import com.seeu.ywq.photography.service.PhotographyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Api(tags = "摄影集（下载作品）", description = "管理员根据用户 uid 上传摄影作品")
@RestController("adminPhotographyApi")
@RequestMapping("/api/admin/v1/photographs")
//@PreAuthorize("hasRole('ADMIN')")
public class PhotographyApi {
    @Autowired
    private PhotographyService photographyService;
    @Autowired
    private PhotographyDayService photographyDayService;

    @ApiOperation(value = "上传摄影图片", notes = "上传摄影图集，并指定用户 uid，可以分批次多次上传，以 uid 为唯一标识")
    @PostMapping("/{uid}")
    public ResponseEntity postPhotographs(@PathVariable Long uid,
                                            Integer day,
                                            MultipartFile[] images) {
        try {
            List<Photography> photographs = photographyService.save(uid, images);
            PhotographyDay photographyDay = new PhotographyDay();
            photographyDay.setUid(uid);
            photographyDay.setTerminateTime(new Date(new Date().getTime() + day * 86400000)); // 这么多天之后
            photographyDayService.save(photographyDay);
            return ResponseEntity.ok(photographs);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(R.code(500).message("文件上传失败").build());
        }
    }
}
