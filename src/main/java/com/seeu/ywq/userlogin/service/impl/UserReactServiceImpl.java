package com.seeu.ywq.userlogin.service.impl;

import com.seeu.ywq.api.admin.user.USERLogin;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.user.dvo.SimpleUserVO;
import com.seeu.ywq.userlogin.exception.NickNameSetException;
import com.seeu.ywq.userlogin.exception.PasswordSetException;
import com.seeu.ywq.userlogin.exception.PhoneNumberHasUsedException;
import com.seeu.ywq.userlogin.service.UserSignUpService;
import com.seeu.ywq.utils.AppVOUtils;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.user.model.UserLike;
import com.seeu.ywq.user.model.UserLikePKeys;
import com.seeu.ywq.user.repository.UserLikeRepository;
import com.seeu.ywq.user.repository.UserInfoRepository;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.userlogin.dvo.UserLoginVO;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.uservip.model.UserVIP;
import com.seeu.ywq.userlogin.repository.UserLoginRepository;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class UserReactServiceImpl implements UserReactService {
    @Resource
    private UserLoginRepository userLoginRepository;
    @Resource
    private UserLikeRepository userLikeRepository;
    @Resource
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AppVOUtils appVOUtils;
    @Autowired
    private UserSignUpService userSignUpService;

    @Transactional
    @Override
    public STATUS likeMe(Long myUid, Long hisUid) {
        if (myUid.equals(hisUid))
            return STATUS.contradiction;
        if (userLikeRepository.exists(new UserLikePKeys(myUid, hisUid)))
            return STATUS.exist;
        userInfoService.likeMePlusOne(hisUid);
        UserLike like = new UserLike();
        like.setCreateTime(new Date());
        like.setLikedUid(hisUid);
        like.setUid(myUid);
        userLikeRepository.save(like);
        return STATUS.success;
    }

    @Transactional
    @Override
    public STATUS cancelLikeMe(Long myUid, Long hisUid) {
        if (myUid.equals(hisUid))
            return STATUS.contradiction;
        if (!userLikeRepository.exists(new UserLikePKeys(myUid, hisUid)))
            return STATUS.not_exist;
        userLikeRepository.delete(new UserLikePKeys(myUid, hisUid));
        userInfoService.likeMeMinsOne(hisUid);
        return STATUS.success;
    }

    @Override
    public Boolean hasLikedHer(Long uid, Long herUid) {
        return null != userLikeRepository.findOne(new UserLikePKeys(uid, herUid));
    }

    @Override
    public BigDecimal calculateDistanceFromHer(BigDecimal longitude, BigDecimal latitude, Long herUid) {
        if (longitude == null || latitude == null) return null;
        UserLogin ul = findOne(herUid);
        if (ul == null || ul.getLatitude() == null || ul.getLongitude() == null) return null;
        return calculateDistance(longitude, latitude, ul.getLongitude(), ul.getLatitude());
    }

    /**
     * @param fromLongitude 经度
     * @param fromLatitude  维度
     * @param toLongitude
     * @param toLatitude
     * @return
     */
    @Override
    public BigDecimal calculateDistance(BigDecimal fromLongitude, BigDecimal fromLatitude, BigDecimal toLongitude, BigDecimal toLatitude) {
//        ACOS(
//                SIN((:lat * 3.1415) /180 )
//                    *SIN((latitude * 3.1415) / 180)
//                + COS((:lat * 3.1415) /180 )
//                    *COS((latitude * 3.1415) / 180)
//                * COS((:lon * 3.1415) /180 - (longitude * 3.1415) / 180 )
//        ) *6380
        double radLat1 = fromLatitude.doubleValue() * Math.PI / 180.0;
        double radLat2 = toLatitude.doubleValue() * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = fromLongitude.doubleValue() * Math.PI / 180.0 - toLongitude.doubleValue() * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        return BigDecimal.valueOf(s * 6378137).setScale(2, BigDecimal.ROUND_UP);//单位米

//        double ratio = 3.141592654 / 180;
//        return BigDecimal.valueOf(Math.acos(
//                Math.sin(fromLatitude.doubleValue() * ratio)
//                        * Math.sin(toLatitude.doubleValue() * ratio)
//                        + Math.cos(fromLongitude.doubleValue() * ratio)
//                        * Math.cos(fromLatitude.doubleValue() * ratio)
//                        * Math.cos(fromLongitude.doubleValue() * ratio
//                        - toLatitude.doubleValue() * ratio)
//        ) * 6380);
    }

    @Override
    public Boolean exists(Long uid) {
        return userLoginRepository.exists(uid);
    }

    @Override
    public UserLogin findOne(Long uid) {
        return userLoginRepository.findOne(uid);
    }

    @Override
    public UserLoginVO findOneWithSafety(Long uid) {
        UserLogin ul = userLoginRepository.findOne(uid);
        UserLoginVO vo = new UserLoginVO();
        BeanUtils.copyProperties(ul, vo);
        return vo;
    }

    @Override
    public UserLogin findByPhone(String phone) {
        return userLoginRepository.findByPhone(phone);
    }

    @Override
    public SimpleUserVO findOneAndTransferToVO(Long visitorUid, Long uid) {
        if (visitorUid == null) {
            UserLogin ul = userLoginRepository.findOne(uid);
            SimpleUserVO userVO = new SimpleUserVO();
            if (ul == null) return userVO;// if ul==null，则直接返回，不填充数据
            BeanUtils.copyProperties(ul, userVO);
            userVO.setLiked(false);
            userVO.setFollowed(false);
//            userVO.setVip(UserVIP.VIP.none);
            return userVO;
        } else {
            List<Object[]> objects = userLoginRepository.queryItsByUid(visitorUid, uid);
            if (objects == null || objects.size() == 0) return null;
            return transferToVO(objects.get(0));
        }
        // vip\liked\followed
    }

    @Override
    public List<SimpleUserVO> findAllSimpleUsers(Long uid, Collection<Long> uids) {
        List<Object[]> list = userLoginRepository.queryItsByUid(uid, uids);
        List<SimpleUserVO> vos = new ArrayList<>();
        for (Object[] objects : list) {
            vos.add(transferToVO(objects));
        }
        return vos;
    }

    private List<SimpleUserVO> transferToVO(List<Object[]> objects) {
        List<SimpleUserVO> vos = new ArrayList<>();
        for (Object[] object : objects) {
            vos.add(transferToVO(object));
        }
        return vos;
    }

    private SimpleUserVO transferToVO(Object[] objects) {
        if (objects == null || objects.length != 9) return null;// 长度必须是 9 个
        SimpleUserVO vo = new SimpleUserVO();
        vo.setUid(appVOUtils.parseLong(objects[0]));
        vo.setNickname(appVOUtils.parseString(objects[1]));
        vo.setHeadIconUrl(appVOUtils.parseString(objects[2]));
        vo.setGender(appVOUtils.parseGENDER(objects[3]));
        vo.setVip(UserVIP.VIP.none);
        UserVIP.VIP vip = appVOUtils.parseVIP(objects[4]);
        Date terTime = appVOUtils.parseDate(objects[5]);
        if (vip == null) vip = UserVIP.VIP.none;
        if (terTime == null || terTime.before(new Date())) vip = UserVIP.VIP.none;
        vo.setVip(vip);
        vo.setIdentifications(appVOUtils.parseBytesToLongList(objects[6]));
        vo.setFollowed(1 == appVOUtils.parseInt(objects[7]) ? true : false);
        vo.setLiked(1 == appVOUtils.parseInt(objects[8]) ? true : false);
        return vo;
    }

    @Override
    public String getPhone(Long uid) {
        UserLogin ul = userLoginRepository.findOne(uid);
        return ul == null ? null : ul.getPhone();
    }

    @Override
    public String getWeChatID(Long uid) {
        User user = userInfoRepository.findOne(uid);
        return user == null ? null : user.getWechat();
    }

    @Override
    public UserLogin save(UserLogin userLogin) {
        if (userLogin == null) return null;
        return userLoginRepository.save(userLogin);
    }

    @Override
    public UserLogin saveNickName(Long uid, String nickname) {
        UserLogin ul = userLoginRepository.findOne(uid);
        if (ul != null) {
            ul.setNickname(nickname);
            return userLoginRepository.save(ul);
        }
        return null;
    }

    @Override
    public Page<SimpleUserVO> search(Long visitorUid, String word, Pageable pageable) {
        List<Object[]> list = null;
        Integer pageStart = pageable.getPageNumber() * pageable.getPageSize();
        Integer size = pageable.getPageSize();
        if (visitorUid == null || visitorUid == 0)
            list = userLoginRepository.searchIts("%" + word + "%", pageStart, size);
        else
            list = userLoginRepository.searchItsByUid(visitorUid, "%" + word + "%", pageStart, size);
        Integer count = userLoginRepository.countAllByUidNotNull();
        return new PageImpl<>(transferToVO(list), pageable, count);
    }

    @Override
    public Page<UserLogin> findAll(Pageable pageable) {
        return userLoginRepository.findAll(pageable);
    }

    @Override
    public Page<UserLogin> searchAll(USERLogin searchType, String word, Pageable pageable) throws ActionParameterException {
        if (searchType == null || word == null || "".equals(word.trim()))
            return findAll(pageable);
        if (searchType == USERLogin.phone || searchType == USERLogin.uid)
            try {
                if (Long.parseLong(word) < 0)
                    throw new ActionParameterException("参数必须为正整数数字");
            } catch (Exception e) {
                throw new ActionParameterException("参数必须为正整数数字");
            }
        if (searchType == USERLogin.uid)
            return userLoginRepository.findAllByUid(Long.parseLong(word), pageable);
        if (searchType == USERLogin.phone)
            return userLoginRepository.findAllByPhoneLike("%" + word + "%", pageable);
        if (searchType == USERLogin.nickname)
            return userLoginRepository.findAllByUsernameLike("%" + word + "%", pageable);
        return findAll(pageable);
    }

    @Override
    public UserLogin add(String phone, String nickname, String password, UserLogin.GENDER gender, String headIconUrl, User user) throws NickNameSetException, PhoneNumberHasUsedException, PasswordSetException {
        UserLogin ul = userSignUpService.signUpByAdmin(nickname, phone, password);
        if (ul != null) {
            ul.setGender(gender);
            ul.setHeadIconUrl(headIconUrl);
            ul = userLoginRepository.save(ul);
        }
        Long uid = ul.getUid();
        user.setUid(uid);
        userInfoRepository.save(user);
        return ul;
    }

    @Override
    public Page<UserLogin> findAllByUids(Collection<Long> uids, Pageable pageable) {
        return userLoginRepository.findAllByUidIn(uids, pageable);
    }
}
