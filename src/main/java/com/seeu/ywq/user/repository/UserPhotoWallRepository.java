package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.PhotoWall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * 照片墙最多 5 张照片
 */
public interface UserPhotoWallRepository extends JpaRepository<PhotoWall, Long> {
    // 找到所有的未删除的相片
    List<PhotoWall> findAllByUidAndDeleteFlag(@Param("uid") Long uid, @Param("deleteFlag") PhotoWall.PHOTO_WALL_DELETE_FLAG deleteFlag);

    // 找到最新的一张照片作为封面
    PhotoWall findFirstByUidAndDeleteFlagOrderByCreateTimeDesc(@Param("uid") Long uid, @Param("deleteFlag") PhotoWall.PHOTO_WALL_DELETE_FLAG deleteFlag);

    // 当前照片数量
    Integer countAllByUidAndDeleteFlag(@Param("uid") Long uid, @Param("deleteFlag") PhotoWall.PHOTO_WALL_DELETE_FLAG deleteFlag);

    PhotoWall findByUidAndId(@Param("uid") Long uid, @Param("id") Long id);

    List<PhotoWall> findAllByUidAndIdIn(@Param("uid") Long uid, @Param("ids") Collection<Long> ids);

//    void deleteByUidAndId(@Param("uid") Long uid, @Param("id") Long id);
//
//    void deleteAllByUidAndIdIn(@Param("uid") Long uid, @Param("ids") Collection<Long> ids);
}
