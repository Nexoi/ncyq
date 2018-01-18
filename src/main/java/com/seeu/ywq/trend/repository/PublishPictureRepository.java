package com.seeu.ywq.trend.repository;

import com.seeu.ywq.trend.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PublishPictureRepository extends JpaRepository<Picture, Long> {
    // find open
    Picture findFirstByUidAndDeleteFlagOrderByCreateTime(@Param("uid") Long uid, @Param("deleteFlag") Picture.DELETE_FLAG deleteFlag);

    // find close
    Picture findFirstByUidAndDeleteFlagAndImageCloseNotNullOrderByCreateTime(@Param("uid") Long uid, @Param("deleteFlag") Picture.DELETE_FLAG deleteFlag);

    // find by albumType
    Picture findFirstByUidAndAlbumTypeAndDeleteFlagOrderByCreateTimeDesc(@Param("uid") Long uid, @Param("albumType") Picture.ALBUM_TYPE albumType, @Param("deleteFlag") Picture.DELETE_FLAG deleteFlag);

}
