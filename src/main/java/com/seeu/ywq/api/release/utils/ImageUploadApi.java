package com.seeu.ywq.api.release.utils;

import com.seeu.core.R;
import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/upload/image")
@PreAuthorize("hasRole('USER')")
public class ImageUploadApi {
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity upload(MultipartFile file) {
        try {
            String url = fileUploadService.upload(file);
            Map map = new HashMap();
            map.put("url", url);
            return ResponseEntity.ok(map);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(R.code(500).message("上传失败！服务器异常"));
        }
    }

    @PostMapping("/with-thumb")
    public ResponseEntity uploadFull(MultipartFile file) {
        try {
            Image image = fileUploadService.uploadImage(file);
            image = imageService.save(image);
            return ResponseEntity.ok(image);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(R.code(500).message("上传失败！服务器异常"));
        }
    }

}
