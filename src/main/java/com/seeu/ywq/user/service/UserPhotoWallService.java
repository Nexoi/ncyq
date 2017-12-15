package com.seeu.ywq.user.service;

import com.seeu.ywq.user.dto.PhotoWallVO;
import com.seeu.ywq.user.model.PhotoWall;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserPhotoWallService {
    List<PhotoWall> findAllByUid(Long uid);

    List<PhotoWallVO> saveImages(Long uid, MultipartFile[] images) throws Exception;
}
