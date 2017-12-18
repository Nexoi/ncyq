package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByIdAndDeleteFlag(@Param("id") Long id, @Param("deleteFlag") Tag.DELETE_FLAG deleteFlag);

    Page findAllByDeleteFlag(@Param("deleteFlag") Tag.DELETE_FLAG deleteFlag, Pageable pageable);

    List<Tag> findAllByDeleteFlag(@Param("deleteFlag") Tag.DELETE_FLAG deleteFlag);
}
