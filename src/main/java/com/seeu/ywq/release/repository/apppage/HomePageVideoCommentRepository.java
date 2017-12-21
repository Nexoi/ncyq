package com.seeu.ywq.release.repository.apppage;

import com.seeu.ywq.release.model.apppage.HomePageVideo;
import com.seeu.ywq.release.model.apppage.HomePageVideoComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface HomePageVideoCommentRepository extends JpaRepository<HomePageVideoComment, Long> {
    Page<HomePageVideoComment> findAllByVideoIdAndDeleteFlag(@Param("videoId") Long videoId,
                                                                        @Param("deleteFlag") HomePageVideoComment.DELETE_FLAG deleteFlag,
                                                                        Pageable pageable);

    Page<HomePageVideoComment> findAllByVideoIdAndDeleteFlagAndFatherIdIsNull(@Param("videoId") Long videoId,
                                                                              @Param("deleteFlag") HomePageVideoComment.DELETE_FLAG deleteFlag,
                                                                              Pageable pageable);
}
