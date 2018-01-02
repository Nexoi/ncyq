package com.seeu.ywq.page.repository;

import com.seeu.ywq.page.model.HomePageVideoComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface HomePageVideoCommentRepository extends JpaRepository<HomePageVideoComment, Long> {
    HomePageVideoComment findByVideoIdAndDeleteFlag(@Param("videoId") Long videoId,
                                                    @Param("deleteFlag") HomePageVideoComment.DELETE_FLAG deleteFlag);

    Page<HomePageVideoComment> findAllByVideoIdAndDeleteFlagAndFatherIdIsNull(@Param("videoId") Long videoId,
                                                                              @Param("deleteFlag") HomePageVideoComment.DELETE_FLAG deleteFlag,
                                                                              Pageable pageable);
}
