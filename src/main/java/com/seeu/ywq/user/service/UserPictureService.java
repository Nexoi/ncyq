package com.seeu.ywq.user.service;

import com.seeu.ywq.user.dto.PhotoWallVO;
import com.seeu.ywq.user.dto.PictureVO;
import com.seeu.ywq.user.model.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserPictureService {
    /**
     * @param targetUid 目标用户
     * @param myUid     登陆用户
     * @return
     */
    Page findAllByUid(Long targetUid, Long myUid, PageRequest pageRequest);

    /**
     * @param targetUid 目标用户【本人】
     * @return
     */
    Page findAllByUid(Long targetUid, Picture.ALBUM_TYPE albumType, PageRequest pageRequest);

    /**
     * 根据不同的 uid 访问不同的相册图片时候会得到不同的图片地址
     *
     * @param publishId
     * @param uid
     * @return
     */
    List<PictureVO> findAllByPublishId(Long publishId, Long uid);

    /**
     * 匿名访问，私密相册照片必须全部为模糊照片
     *
     * @param publishId
     * @return
     */
    List<PictureVO> findAllByPublishId(Long publishId);

    /**
     * 存储图片（公开/私密），并持久化
     *
     * @param uid
     * @param publishId
     * @param albumTypes
     * @param images
     * @return
     * @throws Exception
     */
    List<PictureVO> savePictures(Long uid, Long publishId, Picture.ALBUM_TYPE[] albumTypes, MultipartFile[] images) throws Exception;

    /**
     * 发布内容时使用，不持久化 Picture，但会持久化 Image（阿里云存储后即会持久化到本地数据库）
     *
     * @param uid
     * @param publishId
     * @param albumTypes
     * @param images
     * @return
     * @throws Exception
     */
    List<Picture> getPictureWithOutSave(Long uid, Long publishId, Picture.ALBUM_TYPE[] albumTypes, MultipartFile[] images) throws Exception;

    boolean canVisit(Long uid, Long pictureId);

    /**
     * 匿名访问时
     *
     * @param picture
     * @return
     */
    PictureVO transferToVO(Picture picture);

    /**
     * 实名访问时，包括【本人】访问
     *
     * @param picture
     * @param uid
     * @return
     */
    PictureVO transferToVO(Picture picture, Long uid);

    /**
     * 匿名访问时
     *
     * @param pictures
     * @return
     */
    List<PictureVO> transferToVO(List<Picture> pictures);

    /**
     * 实名访问时，包括【本人】访问
     *
     * @param pictures
     * @param uid
     * @return
     */
    List<PictureVO> transferToVO(List<Picture> pictures, Long uid);
}
