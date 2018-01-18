package com.seeu.third.filestore;

import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.trend.model.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileUploadService {
    String upload(MultipartFile file) throws IOException;

    Image uploadImage(MultipartFile file) throws IOException;

    List<Image> uploadImages(MultipartFile[] files) throws IOException;

    Map<Picture.ALBUM_TYPE, Image> uploadImageWithPictureType(MultipartFile file, Picture.ALBUM_TYPE type) throws IOException;

    List<Map<Picture.ALBUM_TYPE, Image>> uploadImagesWithPictureTypes(MultipartFile[] files, Picture.ALBUM_TYPE[] types) throws IOException;

    List<Image> uploadImagesWithSeqList(MultipartFile[] files, Picture.ALBUM_TYPE[] types) throws IOException;

    Video uploadVideo(MultipartFile videoFile, MultipartFile coverImage) throws IOException;

    Video uploadVideo(MultipartFile videoFile) throws IOException;
}
