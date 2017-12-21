package com.seeu.ywq.release.service;

import com.seeu.ywq.release.dvo.apppage.PositionUserVO;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * 更新用户位置信息
 * 查找附近的人（根据用户UID，或者经纬度、block）
 */
public interface UserPositionService {

    void updatePosition(Long uid, BigDecimal longitude, BigDecimal latitude);

    // 附近的人

    /**
     * @param distance  测量距离
     * @param longitude 本人经度
     * @param latitude  本人纬度
     * @param pageable
     * @return
     */
    Page<PositionUserVO> findNear(Long distance, BigDecimal longitude, BigDecimal latitude, Pageable pageable);

    Page<PositionUserVO> findNear(UserLogin.GENDER gender, Long distance, BigDecimal longitude, BigDecimal latitude, Pageable pageable);

    // 工具，经纬度 -> block 转换
    Long convertPositionToBlock(BigDecimal mapPosition);
}
