package com.seeu.ywq.user.service.impl;

import com.seeu.file.aliyun_storage.StorageImageService;
import com.seeu.ywq.user.dto.PhotoWallVO;
import com.seeu.ywq.user.dto.PictureVO;
import com.seeu.ywq.user.model.Image;
import com.seeu.ywq.user.model.PhotoWall;
import com.seeu.ywq.user.model.Picture;
import com.seeu.ywq.user.repository.ImageRepository;
import com.seeu.ywq.user.repository.User$ImageRepository;
import com.seeu.ywq.user.repository.User$PictureRepository;
import com.seeu.ywq.user.repository.UserPictureRepository;
import com.seeu.ywq.user.service.UserPictureService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserPictureServiceImpl implements UserPictureService {
    @Resource
    UserPictureRepository userPictureRepository;
    @Resource
    ImageRepository imageRepository;
    @Resource
    User$ImageRepository user$ImageRepository;
    @Resource
    User$PictureRepository user$PictureRepository;
    @Resource
    StorageImageService storageImageService;

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
        Page page = userPictureRepository.findAllByUidAndDeleteFlag(targetUid, Picture.DELETE_FLAG.show, pageRequest);
        List<Picture> pictures = page.getContent();
        List<PictureVO> pictureVOS = new ArrayList<>();
        for (Picture picture : pictures) {
            if (picture == null) continue;
            PictureVO vo = new PictureVO();
            BeanUtils.copyProperties(picture, vo);
            if (picture.getAlbumType() == Picture.ALBUM_TYPE.close) {
                // 私密相册，需要验证权限
                Image image = picture.getImageOpen();
                boolean exist = user$ImageRepository.countAllByUidAndImageId(myUid, image.getId()) != 0; // 查看是否有访问 open 图的权限
                if (exist) {
                    vo.setImage(image); // 返回清晰图片
                } else {
                    vo.setImage(picture.getImageClose()); // 返回模糊照片
                }
            } else {
                vo.setImage(picture.getImageOpen()); // 返回清晰图片
            }
            pictureVOS.add(vo);
        }
        Page voPage = new PageImpl(pictureVOS, pageRequest, page.getTotalElements());
        return voPage;
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
        Page page = userPictureRepository.findAllByUidAndAlbumTypeEqualsAndDeleteFlag(targetUid, albumType, Picture.DELETE_FLAG.show, pageRequest);
        List<Picture> pictures = page.getContent();
        List<PictureVO> pictureVOS = new ArrayList<>();
        for (Picture picture : pictures) {
            if (picture == null) continue;
            PictureVO vo = new PictureVO();
            BeanUtils.copyProperties(picture, vo);
            vo.setImage(picture.getImageOpen()); // 全部设置为清晰图
            pictureVOS.add(vo);
        }
        Page voPage = new PageImpl(pictureVOS, pageRequest, page.getTotalElements());
        return voPage;
    }

    @Override
    public List<PictureVO> findAllByPublishId(Long publishId, Long uid) {
        List<Picture> pictures = userPictureRepository.findAllByPublishIdAndDeleteFlag(publishId, Picture.DELETE_FLAG.show);
        List<PictureVO> pictureVOS = new ArrayList<>();
        for (Picture picture : pictures) {
            if (picture == null) continue;
            PictureVO vo = new PictureVO();
            BeanUtils.copyProperties(picture, vo);
            if (picture.getAlbumType() == Picture.ALBUM_TYPE.open) {
                // 公开相册
                vo.setImage(picture.getImageOpen());
            } else {
                // 私密相册，需要验证权限
                Image image = picture.getImageOpen();
                boolean exist = user$ImageRepository.countAllByUidAndImageId(uid, image.getId()) != 0; // 查看是否有访问 open 图的权限
                if (exist) {
                    vo.setImage(image);
                } else {
                    vo.setImage(picture.getImageClose()); // 返回模糊照片
                }
            }
            pictureVOS.add(vo);
        }
        return pictureVOS;
    }

    /**
     * 匿名用户查看
     *
     * @param publishId
     * @return
     */
    @Override
    public List<PictureVO> findAllByPublishId(Long publishId) {
        List<Picture> pictures = userPictureRepository.findAllByPublishIdAndDeleteFlag(publishId, Picture.DELETE_FLAG.show);
        List<PictureVO> pictureVOS = new ArrayList<>();
        for (Picture picture : pictures) {
            if (picture == null) continue;
            PictureVO vo = new PictureVO();
            BeanUtils.copyProperties(picture, vo);
            if (picture.getAlbumType() == Picture.ALBUM_TYPE.open) {
                // 公开相册
                vo.setImage(picture.getImageOpen());
            } else {
                // 私密相册，需要验证权限，所以设定为模糊图
                vo.setImage(picture.getImageClose());
            }
            pictureVOS.add(vo);
        }
        return pictureVOS;
    }

    /**
     * @param uid
     * @param publishId
     * @param albumTypes 长度必须与 images 长度一致
     * @param images
     * @return
     * @throws Exception
     */
    @Override
    public List<PictureVO> savePictures(Long uid, Long publishId, Picture.ALBUM_TYPE[] albumTypes, MultipartFile[] images) throws Exception {
        if (images == null || images.length == 0) return new ArrayList<>(); // empty
        if (images.length != albumTypes.length) return new ArrayList<>();//empty
        // 一张张存入阿里云或其他服务器
        List<Image> imageList = new ArrayList<>();
        List<PictureVO> pictureVOS = new ArrayList<>();
        List<Picture> pictures = new ArrayList<>();
        StorageImageService.Result result = storageImageService.saveImages(images); // 暂时采用的阿里云 OSS
        if (result != null && result.getStatus() == StorageImageService.Result.STATUS.success) {
            // 拿到返回的图片信息，未持久化
            List<Image> imageListFromStorage = result.getImageList();
            for (int i = 0; i < result.getImageNum(); i++) {
                imageList.add(imageListFromStorage.get(2 * i)); // open 图
                if (albumTypes[i] == Picture.ALBUM_TYPE.close) {
                    imageList.add(imageListFromStorage.get(2 * i + 1)); // close 图
                }
            }
        }

        // 数据持久化到数据库，以后根据此信息进行访问图片
        List<Image> savedImages = imageRepository.save(imageList);
        for (int i = 0; i < savedImages.size(); i++) {
            // 拿第 2n 号图片信息（清晰的）,2n + 1 号图片信息（模糊的）
            Picture picture = new Picture();
            picture.setPublishId(publishId);
            picture.setUid(uid);
            picture.setAlbumType(albumTypes[i]);
            picture.setDeleteFlag(Picture.DELETE_FLAG.show);
            picture.setImageOpen(savedImages.get(i));
            if (albumTypes[i] == Picture.ALBUM_TYPE.close) {
                picture.setImageClose(savedImages.get(++i));
            }
            picture.setCreateTime(new Date());
            pictures.add(picture);
        }
        // 存储到个人照片墙中
        List<Picture> savedPictures = userPictureRepository.save(pictures);
        for (Picture picture : savedPictures) {
            PictureVO vo = new PictureVO();
            BeanUtils.copyProperties(picture, vo);
            // 由于此处是存储操作，用户自己可见，所以返回信息为清晰图片地址即可
            vo.setImage(picture.getImageOpen());
            pictureVOS.add(vo);
        }
        return pictureVOS;
    }

    /**
     * 自个儿用，发布内容时使用
     *
     * @param uid
     * @param publishId
     * @param albumTypes
     * @param images
     * @return
     * @throws Exception
     */
    @Override
    public List<Picture> getPictureWithOutSave(Long uid, Long publishId, Picture.ALBUM_TYPE[] albumTypes, MultipartFile[] images) throws Exception {
        if (images == null || images.length == 0) return new ArrayList<>(); // empty
        if (images.length != albumTypes.length) return new ArrayList<>();//empty
        // 一张张存入阿里云或其他服务器
        List<Image> imageList = new ArrayList<>();
        List<Picture> pictures = new ArrayList<>();
        StorageImageService.Result result = storageImageService.saveImages(images); // 暂时采用的阿里云 OSS
        if (result != null && result.getStatus() == StorageImageService.Result.STATUS.success) {
            // 拿到返回的图片信息，未持久化
            List<Image> imageListFromStorage = result.getImageList();
            for (int i = 0; i < result.getImageNum(); i++) {
                imageList.add(imageListFromStorage.get(2 * i)); // open 图
                if (albumTypes[i] == Picture.ALBUM_TYPE.close) {
                    imageList.add(imageListFromStorage.get(2 * i + 1)); // close 图
                }
            }
        }

        // 数据持久化到数据库，以后根据此信息进行访问图片
        List<Image> savedImages = imageRepository.save(imageList);
        for (int i = 0; i < savedImages.size(); i++) {
            // 拿第 2n 号图片信息（清晰的）,2n + 1 号图片信息（模糊的）
            Picture picture = new Picture();
            picture.setPublishId(publishId);
            picture.setUid(uid);
            picture.setAlbumType(albumTypes[i]);
            picture.setDeleteFlag(Picture.DELETE_FLAG.show);
            picture.setImageOpen(savedImages.get(i));
            if (albumTypes[i] == Picture.ALBUM_TYPE.close) {
                picture.setImageClose(savedImages.get(++i));
            }
            picture.setCreateTime(new Date());
            pictures.add(picture);
        }
        return pictures;
    }

    @Override
    public boolean canVisit(Long uid, Long pictureId) {
        return 0 != user$PictureRepository.countAllByUidAndPictureId(uid, pictureId);
    }

    /**
     * 匿名
     *
     * @param picture
     * @return
     */
    @Override
    public PictureVO transferToVO(Picture picture) {
        if (picture == null) return null;
        PictureVO vo = new PictureVO();
        BeanUtils.copyProperties(picture, vo);
        if (Picture.ALBUM_TYPE.close == picture.getAlbumType())
            vo.setImage(picture.getImageClose());
        else {
            vo.setImage(picture.getImageOpen());
        }
        return vo;
    }

    /**
     * 实名或【本人】
     *
     * @param picture
     * @param uid
     * @return
     */
    @Override
    public PictureVO transferToVO(Picture picture, Long uid) {
        if (picture == null) return null;
        PictureVO vo = new PictureVO();
        BeanUtils.copyProperties(picture, vo);
        if (picture.getUid().equals(uid)) {
            // 由于此处是存储操作，用户自己可见，所以返回信息为清晰图片地址即可
            vo.setImage(picture.getImageOpen());
        } else {
            if (Picture.ALBUM_TYPE.close == picture.getAlbumType())
                if (canVisit(uid, picture.getId())) {
                    vo.setImage(picture.getImageOpen());
                } else {
                    vo.setImage(picture.getImageClose());
                }
            else {
                vo.setImage(picture.getImageOpen());
            }
        }
        return vo;
    }

    /**
     * 匿名
     *
     * @param pictures
     * @return
     */
    @Override
    public List<PictureVO> transferToVO(List<Picture> pictures) {
        if (pictures == null || pictures.size() == 0) return new ArrayList<>();
        List<PictureVO> vos = new ArrayList<>();
        for (Picture picture : pictures) {
            vos.add(transferToVO(picture));
        }
        return vos;
    }

    /**
     * 实名或【本人】
     *
     * @param pictures
     * @param uid
     * @return
     */
    @Override
    public List<PictureVO> transferToVO(List<Picture> pictures, Long uid) {
        if (pictures == null || pictures.size() == 0) return new ArrayList<>();
        boolean canVisit = canVisit(uid, pictures.get(0).getPublishId());
        List<PictureVO> vos = new ArrayList<>();
        for (Picture picture : pictures) {
            PictureVO vo = new PictureVO();
            BeanUtils.copyProperties(picture, vo);
            if (picture.getUid().equals(uid)) {
                // 由于此处是存储操作，用户自己可见，所以返回信息为清晰图片地址即可
                vo.setImage(picture.getImageOpen());
            } else {
                if (Picture.ALBUM_TYPE.close == picture.getAlbumType())
                    if (canVisit) {
                        vo.setImage(picture.getImageOpen());
                    } else {
                        vo.setImage(picture.getImageClose());
                    }
                else {
                    vo.setImage(picture.getImageOpen());
                }
            }
            vos.add(vo);
        }
        return vos;
    }

}
