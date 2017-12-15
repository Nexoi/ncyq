package com.seeu.ywq.user.service.impl;

import com.seeu.ywq.user.model.Picture;
import com.seeu.ywq.user.model.Image;
import com.seeu.ywq.user.repository.AliImageRepository;
import com.seeu.ywq.user.repository.User$ImageRepository;
import com.seeu.ywq.user.repository.UserAlbumRepository;
import com.seeu.ywq.user.service.UserAlbumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserAlbumServiceImpl implements UserAlbumService {
    @Resource
    UserAlbumRepository userAlbumRepository;
    @Resource
    AliImageRepository aliImageRepository;
    @Resource
    User$ImageRepository user$ImageRepository;

    /**
     * 给其他人使用的
     *
     * @param targetUid   目标用户
     * @param myUid       登陆用户
     * @param pageRequest
     * @return
     */
    @Override
    public Page findAllByUid(Long targetUid, Long myUid, PageRequest pageRequest) {
        Page page = userAlbumRepository.findAllByUidAndAlbumTypeEqualsAndDeleteFlag(targetUid, Picture.ALBUM_TYPE.open, Picture.DELETE_FLAG.show, pageRequest);
        List<Picture> pictures = page.getContent();
        // 替换 image-url
//        if (pictures.size() != 0) {
//            if (targetUid.equals(myUid)) {
//                // 本人访问，全部设为开放
//                for (Picture picture : pictures) {
//                    if (picture == null || picture.getImageUrl().trim().length() == 0) continue;
//                    Long imageId = Long.parseLong(picture.getImageUrl());// 这是图片的 id
//                    // 替换
//                    Image image = aliImageRepository.findOne(imageId);
//                    if (image == null) continue;
//                    picture.setImageUrl(image.getImageOpenUrl());
//                    picture.setThumbImageUrl(image.getThumbImageOpenUrl());
//                }
//            } else {
//                // 其他人访问，根据权限进行图片选择
//                for (Picture picture : pictures) {
//                    if (picture == null || picture.getImageUrl().trim().length() == 0) continue;
//                    Long imageId = Long.parseLong(picture.getImageUrl());// 这是图片的 id
//                    // 替换
//                    Image image = aliImageRepository.findOne(imageId);
//                    if (image == null) continue;
//                    if (picture.getAlbumType() == Picture.ALBUM_TYPE.close) { // 私密照片
//                        // 寻找用户是否有权限访问原图
//                        Integer count = user$ImageRepository.countAllByUidAndImageId(myUid, imageId);
//                        if (count != null && count != 0) {
//                            picture.setImageUrl(image.getImageOpenUrl());
//                            picture.setThumbImageUrl(image.getThumbImageOpenUrl());
//                        } else {
//                            // 设为私密图片地址
//                            picture.setImageUrl(image.getImageCloseUrl());
//                            picture.setThumbImageUrl(image.getThumbImageCloseUrl());
//                        }
//                    } else {
//                        picture.setImageUrl(image.getImageOpenUrl());
//                        picture.setThumbImageUrl(image.getThumbImageOpenUrl());
//                    }
//                }
//            }
//        }
        return page;
    }

    /**
     * 给本人使用的
     *
     * @param targetUid   目标用户【本人】
     * @param albumType
     * @param pageRequest
     * @return
     */
    @Override
    public Page findAllByUid(Long targetUid, Picture.ALBUM_TYPE albumType, PageRequest pageRequest) {
        Page page = userAlbumRepository.findAllByUidAndAlbumTypeEqualsAndDeleteFlag(targetUid, albumType, Picture.DELETE_FLAG.show, pageRequest);
        List<Picture> pictures = page.getContent();
        // 替换 image-url
//        if (pictures.size() != 0) {
//            for (Picture picture : pictures) {
//                if (picture == null || picture.getImageUrl().trim().length() == 0) continue;
//                Long imageId = Long.parseLong(picture.getImageUrl());// 这是图片的 id
//                // 替换
//                Image image = aliImageRepository.findOne(imageId);
//                if (image == null) continue;
//                picture.setImageUrl(image.getImageOpenUrl());
//                picture.setThumbImageUrl(image.getThumbImageOpenUrl());
//            }
//        }
        return page;
    }

}
