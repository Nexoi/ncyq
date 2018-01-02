package com.seeu.ywq.user.service.impl;

import com.seeu.ywq.page.dvo.PositionUserVO;
import com.seeu.ywq.page.repository.PagePositionUserRepository;
import com.seeu.ywq.page.service.AppVOService;
import com.seeu.ywq.user.service.UserPositionService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class UserPositionServiceImpl implements UserPositionService {
    @Resource
    private UserReactService userReactService;
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
        UserLogin userLogin = userReactService.findOne(uid);
        if (userLogin != null) {
            userLogin.setLongitude(longitude);
            userLogin.setLatitude(latitude);
            userLogin.setPositionBlockY(convertPositionToBlock(latitude));
            userLogin.setPositionBlockX(convertPositionToBlock(longitude));
            userReactService.save(userLogin);
        }
    }

    @Override
    public Page<PositionUserVO> findNear(Long uid, Long distance, BigDecimal longitude, BigDecimal latitude, Pageable pageable) {
//        Page page = pagePositionUserRepository.findAllByPositionBolck(uid, convertPositionToBlock(latitude), convertPositionToBlock(longitude), distance, pb);
//        List<Object[]> list = page.getContent();
//        List<PositionUserVO> voList = appVOService.formPositionUserVO(list, longitude, latitude);
        List list = pagePositionUserRepository.findAllWithDistanceByPositionBlock(uid, (latitude), (longitude), distance, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer totalSize = pagePositionUserRepository.countWithDistancePositionBlock(uid, latitude, longitude, distance);
        List<PositionUserVO> voList = appVOService.formPositionDistanceUserVO(list);
        return new PageImpl<PositionUserVO>(voList, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.ASC, "distance")), totalSize);
    }

    @Override
    public Page<PositionUserVO> findNear(Long uid, UserLogin.GENDER gender, Long distance, BigDecimal longitude, BigDecimal latitude, Pageable pageable) {
//        Page page = pagePositionUserRepository.findAllByPositionBolckAndGender(uid, gender.ordinal(), convertPositionToBlock(latitude), convertPositionToBlock(longitude), distance, pb);
//        List<Object[]> list = page.getContent();
//        List<PositionUserVO> voList = appVOService.formPositionUserVO(list, longitude, latitude);
        List list = pagePositionUserRepository.findAllWithDistanceByPositionBlockAndGender(uid, gender.ordinal(), (latitude), (longitude), distance, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer totalSize = pagePositionUserRepository.countWithDistancePositionBlockGender(uid, gender.ordinal(), latitude, longitude, distance);
        List<PositionUserVO> voList = appVOService.formPositionDistanceUserVO(list);
        return new PageImpl<PositionUserVO>(voList, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.ASC, "distance")), totalSize);
    }

    @Override
    public Long convertPositionToBlock(BigDecimal mapPosition) {
        if (mapPosition == null) return null;
        return mapPosition.multiply(BigDecimal.valueOf(100)).longValue(); // 114.12345678 -> 11412 (km)
    }
}
