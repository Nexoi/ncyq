package com.seeu.ywq.user.service;

import com.seeu.ywq.user.dvo.PhotoWallVO;
import com.seeu.ywq.user.model.PhotoWall;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserPhotoWallService {
    List<PhotoWallVO> findAllByUid(Long uid);

    PhotoWallVO findCoverPhoto(Long uid);

    List<PhotoWallVO> saveImages(Long uid, MultipartFile[] images) throws Exception;

    /**
     * 未删除的照片墙数量
     *
     * @param uid
     * @return
     */
    int countExistPhotos(Long uid);
}
