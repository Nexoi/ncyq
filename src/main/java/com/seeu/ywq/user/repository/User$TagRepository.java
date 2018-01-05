package com.seeu.ywq.user.repository;

import com.seeu.ywq.user.model.UserTag;
import com.seeu.ywq.user.model.UserTagPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface User$TagRepository extends JpaRepository<UserTag, UserTagPKeys> {
    List<UserTag> findAllByUid(@Param("uid") Long uid);

    @Query(value = "select tags_id as tagId, tag_name as tagName from ywq_user_tags as tu join ywq_tag as t on t.id = tu.tags_id where user_uid = ?1", nativeQuery = true)
    List<Object[]> findAllTagsByUid(@Param("uid") Long uid);

    void deleteAllByUid(@Param("uid") Long uid);
}
