package com.seeu.ywq.userlogin.service.impl;

import com.seeu.ywq.page.dvo.SimpleUserVO;
import com.seeu.ywq.page.service.AppVOService;
import com.seeu.ywq.page.utils.AppVOUtils;
import com.seeu.ywq.user.model.User;
import com.seeu.ywq.user.model.UserLike;
import com.seeu.ywq.user.model.UserLikePKeys;
import com.seeu.ywq.user.repository.UserLikeRepository;
import com.seeu.ywq.user.repository.UserInfoRepository;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.userlogin.dvo.UserLoginVO;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.model.UserVIP;
import com.seeu.ywq.userlogin.repository.UserLoginRepository;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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
    public SimpleUserVO findOneAndTransferToVO(Long uid) {
        UserLogin ul = userLoginRepository.findOne(uid);
        SimpleUserVO userVO = new SimpleUserVO();
        if (ul == null) return userVO;// if ul==null，则直接返回，不填充数据
        BeanUtils.copyProperties(ul, userVO);

        // vip\liked\followed
        return userVO;
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
}
