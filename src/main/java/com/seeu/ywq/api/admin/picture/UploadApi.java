package com.seeu.ywq.api.admin.picture;

import com.seeu.core.R;
import com.seeu.third.filestore.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suneo.
 * User: neo
 * Date: 19/01/2018
 * Time: 5:19 PM
 * Describe:
 */


@Api(tags = "上传接口", description = "上传图片、视频，返回 URL")
@RestController("adminUploadApi")
@RequestMapping("/api/admin/v1")
public class UploadApi {

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("图片上传")
    @PostMapping("/picture/upload")
    public ResponseEntity uploadPicture(MultipartFile image) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadImage(image));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(R.code(500).message("上传失败！"));
        }
    }

    @ApiOperation("视频上传")
    @PostMapping("/video/upload")
    public ResponseEntity uploadVideo(MultipartFile video) {
        try {
            Map map = new HashMap();
            map.put("url", fileUploadService.upload(video));
            return ResponseEntity.ok(map);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(R.code(500).message("上传失败！"));
        }
    }

    @ApiOperation("文件上传")
    @PostMapping("/file/upload")
    public ResponseEntity uploadFile(MultipartFile file) {
        try {
            Map map = new HashMap();
            map.put("url", fileUploadService.upload(file));
            return ResponseEntity.ok(map);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(R.code(500).message("上传失败！"));
        }
    }
}
