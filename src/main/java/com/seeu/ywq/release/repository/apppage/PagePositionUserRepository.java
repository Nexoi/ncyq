package com.seeu.ywq.release.repository.apppage;

import com.seeu.ywq.release.model.apppage.HomePageUser;
import com.seeu.ywq.release.model.apppage.HomePageUserPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

// Entity 随便加的
public interface PagePositionUserRepository extends JpaRepository<HomePageUser, HomePageUserPKeys> {

//    List<Object[]> findAllByPosition(@Param("longitude") Long longitude, @Param("latitude") Long latitude);

    @Query(value = "select ul.uid, ul.nickname, ul.head_icon_url,GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, ul.longitude, ul.latitude from ywq_user_login ul left join ywq_user_identifications iden on iden.uid = ul.uid where ul.position_block_x between :blockX - :distance and :blockX + :distance and ul.position_block_y between :blockY - :distance and :blockY + :distance and ul.uid <> :uid group by ul.uid ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<Object[]> findAllByPositionBolck(@Param("uid") Long uid, @Param("blockY") Long blockY, @Param("blockX") Long blockX, @Param("distance") Long distance, Pageable pageable);

    @Query(value = "select ul.uid, ul.nickname, ul.head_icon_url,GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, ul.longitude, ul.latitude from ywq_user_login ul left join ywq_user_identifications iden on iden.uid = ul.uid where ul.gender = :gender and ul.position_block_x between :blockX - :distance and :blockX + :distance and ul.position_block_y between :blockY - :distance and :blockY + :distance and ul.uid <> :uid group by ul.uid ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<Object[]> findAllByPositionBolckAndGender(@Param("uid") Long uid, @Param("gender") Integer gender, @Param("blockY") Long blockY, @Param("blockX") Long blockX, @Param("distance") Long distance, Pageable pageable);

    @Query(value = "select ul.uid, ul.nickname, ul.head_icon_url,GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, ACOS(SIN((:lat * 3.1415) / 180 ) *SIN((latitude * 3.1415) / 180 ) +COS((:lat * 3.1415) / 180 ) * COS((latitude * 3.1415) / 180 ) *COS((:lon * 3.1415) / 180 - (longitude * 3.1415) / 180 ) ) * 6380 as distance from ywq_user_login ul left join ywq_user_identifications iden on iden.uid = ul.uid where ul.uid <> :uid and ul.position_block_x between :lon * 100 - :distance and :lon * 100 + :distance and ul.position_block_y between :lat * 100 - :distance and :lat * 100 + :distance group by ul.uid order by distance limit :startIndex, :pageSize", nativeQuery = true)
    List<Object[]> findAllWithDistanceByPositionBlock(@Param("uid") Long uid, @Param("lat") BigDecimal lat, @Param("lon") BigDecimal lon, @Param("distance") Long distance, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    @Query(value = "select ul.uid, ul.nickname, ul.head_icon_url,GROUP_CONCAT(iden.identification_id SEPARATOR ',') identification_ids, ACOS(SIN((:lat * 3.1415) / 180 ) *SIN((latitude * 3.1415) / 180 ) +COS((:lat * 3.1415) / 180 ) * COS((latitude * 3.1415) / 180 ) *COS((:lon * 3.1415) / 180 - (longitude * 3.1415) / 180 ) ) * 6380 as distance from ywq_user_login ul left join ywq_user_identifications iden on iden.uid = ul.uid where ul.gender = :gender and ul.uid <> :uid and ul.position_block_x between :lon * 100 - :distance and :lon * 100 + :distance and ul.position_block_y between :lat * 100 - :distance and :lat * 100 + :distance group by ul.uid order by distance limit :startIndex, :pageSize", nativeQuery = true)
    List<Object[]> findAllWithDistanceByPositionBlockAndGender(@Param("uid") Long uid, @Param("gender") Integer gender, @Param("lat") BigDecimal lat, @Param("lon") BigDecimal lon, @Param("distance") Long distance, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    @Query(value = "select count(ul.uid) from ywq_user_login ul where ul.uid <> :uid and ul.position_block_x between :lon * 100 - :distance and :lon * 100 + :distance and ul.position_block_y between :lat * 100 - :distance and :lat * 100 + :distance", nativeQuery = true)
    Integer countWithDistancePositionBlock(@Param("uid") Long uid, @Param("lat") BigDecimal lat, @Param("lon") BigDecimal lon, @Param("distance") Long distance);

    @Query(value = "select count(ul.uid) from ywq_user_login ul where ul.gender = :gender and ul.uid <> :uid and ul.position_block_x between :lon * 100 - :distance and :lon * 100 + :distance and ul.position_block_y between :lat * 100 - :distance and :lat * 100 + :distance", nativeQuery = true)
    Integer countWithDistancePositionBlockGender(@Param("uid") Long uid, @Param("gender") Integer gender, @Param("lat") BigDecimal lat, @Param("lon") BigDecimal lon, @Param("distance") Long distance);

}
