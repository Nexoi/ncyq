package com.seeu.ywq.release.service.apppage;

import com.seeu.ywq.release.dvo.apppage.HomePageVOUser;
import com.seeu.ywq.release.dvo.apppage.HomePageVOVideo;
import com.seeu.ywq.release.dvo.apppage.PositionUserVO;
import com.seeu.ywq.release.model.apppage.PublishLite;

import java.math.BigDecimal;
import java.util.List;

/**
 * native sql 查询结果转换为对应 VO
 */
public interface AppVOService {

    // 将 select 出来的结果转换为 vo
    HomePageVOUser formUserVO(Object[] objects);

    List<HomePageVOUser> formUserVO(List<Object[]> objects);

    HomePageVOVideo formVideoVO(Object[] objects);

    List<HomePageVOVideo> formVideoVO(List<Object[]> objects);

    // select 不带 distance，需要 java 计算
    PositionUserVO formPositionUserVO(Object[] objects, BigDecimal longitude, BigDecimal latitude);

    List<PositionUserVO> formPositionUserVO(List<Object[]> objects, BigDecimal longitude, BigDecimal latitude);

    // select 自带 distance
    PositionUserVO formPositionDistanceUserVO(Object[] objects);

    List<PositionUserVO> formPositionDistanceUserVO(List<Object[]> objects);

    // vo for publish
    PublishLite formPublishLite(Object[] objects);

    List<PublishLite> formPublishLite(List<Object[]> objects);
}
