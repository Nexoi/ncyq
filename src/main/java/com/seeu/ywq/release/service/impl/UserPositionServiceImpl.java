package com.seeu.ywq.release.service.impl;

import com.seeu.ywq.release.dvo.apppage.PositionUserVO;
import com.seeu.ywq.release.repository.apppage.PagePositionUserRepository;
import com.seeu.ywq.release.service.AppVOService;
import com.seeu.ywq.release.service.UserPositionService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class UserPositionServiceImpl implements UserPositionService {
    @Resource
    private UserLoginRepository userLoginRepository;
    @Resource
    private PagePositionUserRepository pagePositionUserRepository;
    @Autowired
    private AppVOService appVOService;

    /**
     * @param uid
     * @param longitude 经度
     * @param latitude  纬度
     */
    @Override
    public void updatePosition(Long uid, BigDecimal longitude, BigDecimal latitude) {
        UserLogin userLogin = userLoginRepository.findOne(uid);
        if (userLogin != null) {
            userLogin.setLongitude(longitude);
            userLogin.setLatitude(latitude);
            userLogin.setPositionBlockY(convertPositionToBlock(latitude));
            userLogin.setPositionBlockX(convertPositionToBlock(longitude));
            userLoginRepository.save(userLogin);
        }
    }

    @Override
    public Page<PositionUserVO> findNear(Long distance, BigDecimal longitude, BigDecimal latitude, Pageable pageable) {
        Page page = pagePositionUserRepository.findAllByPositionBolck(convertPositionToBlock(latitude), convertPositionToBlock(longitude), distance, pageable);
        List<Object[]> list = page.getContent();
        List<PositionUserVO> voList = appVOService.formPositionUserVO(list, longitude, latitude);
        return new PageImpl<PositionUserVO>(voList, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.ASC, "distance")), page.getTotalElements());
    }

    @Override
    public Page<PositionUserVO> findNear(UserLogin.GENDER gender, Long distance, BigDecimal longitude, BigDecimal latitude, Pageable pageable) {
        Page page = pagePositionUserRepository.findAllByPositionBolckAndGender(gender.ordinal(), convertPositionToBlock(latitude), convertPositionToBlock(longitude), distance, pageable);
        List<Object[]> list = page.getContent();
        List<PositionUserVO> voList = appVOService.formPositionUserVO(list, longitude, latitude);
        return new PageImpl<PositionUserVO>(voList, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.ASC, "distance")), page.getTotalElements());
    }

    @Override
    public Long convertPositionToBlock(BigDecimal mapPosition) {
        if (mapPosition == null) return null;
        return mapPosition.multiply(BigDecimal.valueOf(100)).longValue(); // 114.12345678 -> 11412 (km)
    }
}
