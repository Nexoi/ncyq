package com.seeu.system.qiniu;

import com.seeu.ywq.release.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
    String upload(MultipartFile file) throws IOException;

    Image uploadWithGetFullInfo(MultipartFile file) throws IOException;
}
