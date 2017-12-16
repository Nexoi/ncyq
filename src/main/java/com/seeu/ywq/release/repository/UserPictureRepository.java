package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPictureRepository extends JpaRepository<Picture, Long> {

    // 找到所有的私密/公开相片
    Page findAllByUidAndAlbumTypeEqualsAndDeleteFlag(@Param("uid") Long uid, @Param("albumType") Picture.ALBUM_TYPE albumType, @Param("deleteFlag") Picture.DELETE_FLAG delete_flag, Pageable pageable);

    // 找到所有的照片
    Page findAllByUidAndDeleteFlag(@Param("uid") Long uid, @Param("deleteFlag") Picture.DELETE_FLAG delete_flag, Pageable pageable);

    // 当前私密/公开照片数量
    Integer countAllByUidAndAlbumTypeEqualsAndDeleteFlag(@Param("uid") Long uid, @Param("deleteFlag") Picture.DELETE_FLAG delete_flag, @Param("albumType") Picture.ALBUM_TYPE albumType);

    // 找到当前发布内容里的所有图片
    List<Picture> findAllByPublishIdAndDeleteFlag(@Param("publishId") Long publishId, @Param("deleteFlag") Picture.DELETE_FLAG delete_flag);
}
