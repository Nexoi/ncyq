package com.seeu.ywq.user.service.impl;

import com.seeu.ywq.user.dvo.PositionUserVO;
import com.seeu.ywq.user.repository.PositionUserRepository;
import com.seeu.ywq.utils.AppVOUtils;
import com.seeu.ywq.user.service.UserPositionService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserPositionServiceImpl implements UserPositionService {
    @Resource
    private UserReactService userReactService;
    @Resource
    private PositionUserRepository positionUserRepository;
    @Autowired
    private AppVOUtils appVOUtils;


    @Override
    public BigDecimal[] findMyPosition(Long uid) {
        UserLogin userLogin = userReactService.findOne(uid);
        if (userLogin != null) {
            BigDecimal[] position = new BigDecimal[2];
            position[0] = userLogin.getLongitude();
            position[1] = userLogin.getLatitude();
            return position;
        }

        return new BigDecimal[0];
    }

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
//        Page page = positionUserRepository.findAllByPositionBolck(uid, convertPositionToBlock(latitude), convertPositionToBlock(longitude), distance, pb);
//        List<Object[]> list = page.getContent();
//        List<PositionUserVO> voList = appVOService.formPositionUserVO(list, longitude, latitude);
        if (uid == null) uid = 0L;
        List list = positionUserRepository.findAllWithDistanceByPositionBlock(uid, (latitude), (longitude), distance, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer totalSize = positionUserRepository.countWithDistancePositionBlock(uid, latitude, longitude, distance);
        List<PositionUserVO> voList = formPositionDistanceUserVO(list);
        return new PageImpl<PositionUserVO>(voList, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.ASC, "distance")), totalSize);
    }

    @Override
    public Page<PositionUserVO> findNear(Long uid, UserLogin.GENDER gender, Long distance, BigDecimal longitude, BigDecimal latitude, Pageable pageable) {
//        Page page = positionUserRepository.findAllByPositionBolckAndGender(uid, gender.ordinal(), convertPositionToBlock(latitude), convertPositionToBlock(longitude), distance, pb);
//        List<Object[]> list = page.getContent();
//        List<PositionUserVO> voList = appVOService.formPositionUserVO(list, longitude, latitude);
        if (uid == null) uid = 0L;
        List list = positionUserRepository.findAllWithDistanceByPositionBlockAndGender(uid, gender.ordinal(), (latitude), (longitude), distance, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer totalSize = positionUserRepository.countWithDistancePositionBlockGender(uid, gender.ordinal(), latitude, longitude, distance);
        List<PositionUserVO> voList = formPositionDistanceUserVO(list);
        return new PageImpl<PositionUserVO>(voList, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.ASC, "distance")), totalSize);
    }

    @Override
    public Long convertPositionToBlock(BigDecimal mapPosition) {
        if (mapPosition == null) return null;
        return mapPosition.multiply(BigDecimal.valueOf(100)).longValue(); // 114.12345678 -> 11412 (km)
    }

    private PositionUserVO formPositionUserVO(Object[] objects, BigDecimal longitude, BigDecimal latitude) {
        if (objects == null || objects.length != 6) return null;// 长度必须是 6 个
        PositionUserVO vo = new PositionUserVO();
        vo.setUid(appVOUtils.parseLong(objects[0]));
        vo.setNickname(appVOUtils.parseString(objects[1]));
        vo.setHeadIconUrl(appVOUtils.parseString(objects[2]));
        vo.setIdentifications(appVOUtils.parseBytesToLongList(objects[3]));
        if (objects[4] == null || objects[5] == null)
            return vo;
        Double longitudeDlt = appVOUtils.parseDouble(objects[4]) - longitude.doubleValue();
        Double latitudeDlt = appVOUtils.parseDouble(objects[5]) - latitude.doubleValue();
        Double radius = Math.sqrt(longitudeDlt * longitudeDlt + latitudeDlt * latitudeDlt);
        vo.setDistance(BigDecimal.valueOf(radius * 100000).setScale(2, BigDecimal.ROUND_UP)); // 转换成米
        return vo;
    }

    private List<PositionUserVO> formPositionUserVO(List<Object[]> objects, BigDecimal longitude, BigDecimal latitude) {
        if (objects == null || objects.size() == 0) return new ArrayList<>();
        List<PositionUserVO> vos = new ArrayList<>();
        for (Object[] object : objects) {
            vos.add(formPositionUserVO(object, longitude, latitude));
        }
        return vos;
    }

    private PositionUserVO formPositionDistanceUserVO(Object[] objects) {
        if (objects == null || objects.length != 5) return null;// 长度必须是 5 个
        PositionUserVO vo = new PositionUserVO();
        vo.setUid(appVOUtils.parseLong(objects[0]));
        vo.setNickname(appVOUtils.parseString(objects[1]));
        vo.setHeadIconUrl(appVOUtils.parseString(objects[2]));
        vo.setIdentifications(appVOUtils.parseBytesToLongList(objects[3]));
        if (objects[4] == null)
            return vo;
        vo.setDistance(BigDecimal.valueOf(appVOUtils.parseDouble(objects[4])).setScale(2, BigDecimal.ROUND_UP)); // 千米
        return vo;
    }

    private List<PositionUserVO> formPositionDistanceUserVO(List<Object[]> objects) {
        if (objects == null || objects.size() == 0) return new ArrayList<>();
        List<PositionUserVO> vos = new ArrayList<>();
        for (Object[] object : objects) {
            vos.add(formPositionDistanceUserVO(object));
        }
        return vos;
    }

}
