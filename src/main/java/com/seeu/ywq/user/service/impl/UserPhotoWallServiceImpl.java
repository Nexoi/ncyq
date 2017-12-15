package com.seeu.ywq.user.service.impl;

import com.seeu.file.aliyun_storage.StorageImageService;
import com.seeu.ywq.user.dto.PhotoWallVO;
import com.seeu.ywq.user.model.PhotoWall;
import com.seeu.ywq.user.model.Image;
import com.seeu.ywq.user.repository.AliImageRepository;
import com.seeu.ywq.user.repository.UserPhotoWallRepository;
import com.seeu.ywq.user.service.UserPhotoWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserPhotoWallServiceImpl implements UserPhotoWallService {
    @Resource
    UserPhotoWallRepository userPhotoWallRepository;
    @Resource
    AliImageRepository aliImageRepository;
    @Autowired
    StorageImageService storageImageService;


    @Override
    public List<PhotoWall> findAllByUid(Long uid) {
//        List<PhotoWall> albumWalls = userPhotoWallRepository.findAllByUidAndDeleteFlag(uid, PhotoWall.PHOTO_WALL_DELETE_FLAG.show);
//        // 替换 image-url
//        if (albumWalls.size() != 0) {
//            for (PhotoWall albumWall : albumWalls) {
//                if (albumWall == null || albumWall.getImageUrl().trim().length() == 0) continue;
//                Long imageId = Long.parseLong(albumWall.getImageUrl());// 这是图片的 id
//                // 替换
//                Image image = aliImageRepository.findOne(imageId);
//                if (image == null) continue;
//                albumWall.setImageUrl(image.getImageOpenUrl());
//                albumWall.setThumbImageUrl(image.getThumbImageOpenUrl());
//            }
//        }
        return null;
    }

    /* 照片墙都是 open 的 */
    @Override
    public List<PhotoWallVO> saveImages(Long uid, MultipartFile[] images) throws Exception {
        if (images == null || images.length == 0) return new ArrayList<>(); // empty
        // 一张张存入阿里云或其他服务器
        List<Image> imageList = new ArrayList<>();
        List<PhotoWallVO> photoWallVOS = new ArrayList<>();
        List<PhotoWall> photoWalls = new ArrayList<>();
        StorageImageService.Result result = storageImageService.saveImages(images);
        if (result != null && result.getStatus() == StorageImageService.Result.STATUS.success) {
            // 拿到返回的图片信息，未持久化
            // 拿第 2n 号图片信息（非模糊的）
            List<Image> imageListFromStorage = result.getImageList();
            for (int i = 0; i < result.getImageNum(); i = 1 + 2) {
                imageList.add(imageListFromStorage.get(0));
            }
        }

        // 数据持久化到数据库，以后根据此信息进行访问图片
        List<Image> savedImages = aliImageRepository.save(imageList);
        for (Image image : savedImages) {
            PhotoWall photoWall = new PhotoWall();
            photoWall.setUid(uid);
            photoWall.setDeleteFlag(PhotoWall.PHOTO_WALL_DELETE_FLAG.show);
            photoWall.setImage(image);
            photoWall.setCreateTime(new Date());
            photoWalls.add(photoWall);
        }
        // 存储到个人照片墙中
        List<PhotoWall> savedPhotoWallList = userPhotoWallRepository.save(photoWalls);
        for (PhotoWall photoWall : savedPhotoWallList) {
            PhotoWallVO photoWallVO = new PhotoWallVO();
            photoWallVO.setId(photoWall.getId());
            photoWallVO.setImage(photoWall.getImage());
            photoWallVOS.add(photoWallVO);
        }
        return photoWallVOS;
    }
}
