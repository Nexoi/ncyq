package com.seeu.ywq.release.repository;

import com.seeu.ywq.release.model.Tag$UserMine;
import com.seeu.ywq.release.model.User$TagPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
@Deprecated
public interface Tag$UserMineRepository extends JpaRepository<Tag$UserMine, User$TagPKeys> {
    @Query(value = "select tag_id as tagId, tag_name as tagName from ywq_tag_with_user_mine as tu join ywq_tag as t on t.id = tu.tag_id where uid = ?1", nativeQuery = true)
    List<Object[]> findAllMineByUid(@Param("uid") Long uid);
}
