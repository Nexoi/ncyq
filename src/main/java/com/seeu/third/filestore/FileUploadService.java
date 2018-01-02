package com.seeu.third.filestore;

import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.model.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String upload(MultipartFile file) throws IOException;

    Image uploadImage(MultipartFile file) throws IOException;

    Video uploadVideo(MultipartFile videoFile, MultipartFile coverImage) throws IOException;

    Video uploadVideo(MultipartFile videoFile) throws IOException;
}
