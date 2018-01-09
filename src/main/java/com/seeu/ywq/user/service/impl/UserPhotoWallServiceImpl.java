package com.seeu.ywq.user.service.impl;

import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.repository.ImageRepository;
import com.seeu.ywq.user.dvo.PhotoWallVO;
import com.seeu.ywq.user.model.PhotoWall;
import com.seeu.ywq.user.repository.UserPhotoWallRepository;
import com.seeu.ywq.user.service.UserPhotoWallService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserPhotoWallServiceImpl implements UserPhotoWallService {
    @Resource
    UserPhotoWallRepository userPhotoWallRepository;
    @Resource
    ImageRepository imageRepository;
    @Autowired
    FileUploadService fileUploadService;


    @Override
    public PhotoWall findOne(Long id) {
        return userPhotoWallRepository.findOne(id);
    }

    @Override
    public Boolean exist(Long uid, Long id) {
        PhotoWall photoWall = userPhotoWallRepository.findByUidAndId(uid, id);
        return !(photoWall == null || photoWall.getDeleteFlag() == PhotoWall.PHOTO_WALL_DELETE_FLAG.delete);
    }

    @Override
    public List<PhotoWallVO> findAllByUid(Long uid) {
        List<PhotoWall> photoWalls = userPhotoWallRepository.findAllByUidAndDeleteFlag(uid, PhotoWall.PHOTO_WALL_DELETE_FLAG.show);
        List<PhotoWallVO> photoWallVOS = new ArrayList<>();
        for (PhotoWall photoWall : photoWalls) {
            PhotoWallVO vo = new PhotoWallVO();
            BeanUtils.copyProperties(photoWall, vo);
            photoWallVOS.add(vo);
        }
        return photoWallVOS;
    }

    @Override
    public PhotoWallVO findCoverPhoto(Long uid) {
        PhotoWall photoWall = userPhotoWallRepository.findFirstByUidAndDeleteFlagOrderByCreateTimeDesc(uid, PhotoWall.PHOTO_WALL_DELETE_FLAG.show);
        if (photoWall == null) return null;
        PhotoWallVO vo = new PhotoWallVO();
        BeanUtils.copyProperties(photoWall, vo);
        return vo;
    }

    /* 照片墙都是 open 的 */
    @Override
    public List<PhotoWallVO> saveImages(Long uid, MultipartFile[] images) throws Exception {
        if (images == null || images.length == 0) return new ArrayList<>(); // empty

        // 换成 七牛 图片上传（不持久化）
        List<PhotoWallVO> photoWallVOS = new ArrayList<>();
        List<PhotoWall> photoWalls = new ArrayList<>();
        List<Image> imageList = fileUploadService.uploadImages(images);

        // 数据持久化到数据库，以后根据此信息进行访问图片
        List<Image> savedImages = imageRepository.save(imageList);
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
            photoWallVO.setUid(photoWall.getUid());
            photoWallVO.setImage(photoWall.getImage());
            photoWallVO.setCreateTime(photoWall.getCreateTime());
            photoWallVOS.add(photoWallVO);
        }
        return photoWallVOS;
    }

    @Override
    public void delete(Long uid, Long id) {
        PhotoWall photoWall = userPhotoWallRepository.findByUidAndId(uid, id);
        if (photoWall != null) {
            photoWall.setDeleteFlag(PhotoWall.PHOTO_WALL_DELETE_FLAG.delete);
            userPhotoWallRepository.save(photoWall);
        }
    }

    @Override
    public int countExistPhotos(Long uid) {
        return userPhotoWallRepository.countAllByUidAndDeleteFlag(uid, PhotoWall.PHOTO_WALL_DELETE_FLAG.show);
    }
}
