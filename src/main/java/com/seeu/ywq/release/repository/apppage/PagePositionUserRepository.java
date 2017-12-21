package com.seeu.ywq.release.repository.apppage;

import com.seeu.ywq.release.model.apppage.HomePageUser;
import com.seeu.ywq.release.model.apppage.HomePageUserPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Entity 随便加的
public interface PagePositionUserRepository extends JpaRepository<HomePageUser, HomePageUserPKeys> {

//    List<Object[]> findAllByPosition(@Param("longitude") Long longitude, @Param("latitude") Long latitude);

    @Query(value = "select ul.uid, ul.nickname, ul.head_icon_url,GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, ul.longitude, ul.latitude from ywq_user_login ul left join ywq_user_identifications iden on iden.uid = ul.uid where ul.position_block_x between :blockX - :distance and :blockX + :distance and ul.position_block_y between :blockY - :distance and :blockY + :distance group by ul.uid ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<Object[]> findAllByPositionBolck(@Param("blockY") Long blockY, @Param("blockX") Long blockX, @Param("distance") Long distance, Pageable pageable);

    @Query(value = "select ul.uid, ul.nickname, ul.head_icon_url,GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, ul.longitude, ul.latitude from ywq_user_login ul left join ywq_user_identifications iden on iden.uid = ul.uid where ul.gender = :gender and ul.position_block_x between :blockX - :distance and :blockX + :distance and ul.position_block_y between :blockY - :distance and :blockY + :distance group by ul.uid ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<Object[]> findAllByPositionBolckAndGender(@Param("gender") Integer gender, @Param("blockY") Long blockY, @Param("blockX") Long blockX, @Param("distance") Long distance, Pageable pageable);
}
