package com.seeu.ywq.api.admin.picture;

import com.seeu.core.R;
import com.seeu.third.filestore.FileUploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by suneo.
 * User: neo
 * Date: 19/01/2018
 * Time: 5:19 PM
 * Describe:
 */


@Api(tags = "图片上传接口", description = "上传图片，返回 URL")
@RestController("adminPictureUploadApi")
@RequestMapping("/api/admin/v1")
public class PictureUploadApi {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/picture/upload")
    public ResponseEntity uploadPicture(MultipartFile image) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadImage(image));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(R.code(500).message("上传失败！"));
        }
    }

    @PostMapping("/video/upload")
    public ResponseEntity uploadVideo(MultipartFile image) {
        try {
            return ResponseEntity.ok(fileUploadService.upload(image));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(R.code(500).message("上传失败！"));
        }
    }
}
