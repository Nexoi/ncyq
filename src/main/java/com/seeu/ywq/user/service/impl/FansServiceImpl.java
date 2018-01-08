package com.seeu.ywq.user.service.impl;

import com.seeu.ywq.exception.ActionNotSupportException;
import com.seeu.ywq.user.dvo.FansIdentificationVO;
import com.seeu.ywq.user.dvo.FansVO;
import com.seeu.ywq.user.model.Fans;
import com.seeu.ywq.user.model.FansPKeys;
import com.seeu.ywq.user.repository.FansRepository;
import com.seeu.ywq.user.service.FansService;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.userlogin.exception.NoSuchUserException;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FansServiceImpl implements FansService {
    @Resource
    private FansRepository fansRepository;
    @Resource
    private UserReactService userReactService;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean hasFollowedHer(Long myUid, Long herUid) {
        return fansRepository.exists(new FansPKeys(myUid, herUid));
    }

    // search 关注列表
    @Override
    public Page searchPageByFansUid(Long uid, String word, Pageable pageable) {
        Page<Object[]> page = fansRepository.searchItByFansUid(uid, word, pageable);
        return new PageImpl(transferToFansVOIgnoreFansUid(page.getContent()), pageable, page.getTotalElements());
    }

    @Override
    public Page searchPageByFansUidWithFullIdentificationInfo(Long uid, String word, Pageable pageable) {
        Page<Object[]> page = fansRepository.searchItByFansUidWithFullIdentificationInfo(uid, word, pageable);
        return new PageImpl(transferToFansVOIgnoreFansUidWithFullIdentificationInfo(page.getContent()), pageable, page.getTotalElements());
    }

    // find 关注列表
    @Override
    public Page findPageByFansUid(Long fansUid, Pageable pageable) {
        Page<Object[]> page = fansRepository.findItByFansUid(fansUid, pageable);
        return new PageImpl(transferToFansVOIgnoreFansUid(page.getContent()), pageable, page.getTotalElements());
    }

    @Override
    public Page findPageByFansUidWithFullIdentificationInfo(Long fansUid, Pageable pageable) {
        Page<Object[]> page = fansRepository.findItByFansUidWithFullIdentificationInfo(fansUid, pageable);
        return new PageImpl(transferToFansVOIgnoreFansUidWithFullIdentificationInfo(page.getContent()), pageable, page.getTotalElements());
    }

    @Override
    public void followSomeone(Long myUid, Long hisUid) throws NoSuchUserException, ActionNotSupportException {
        // 有这个人？
        if (!userReactService.exists(hisUid))
            throw new NoSuchUserException(hisUid);
        // 先查看自己是否已经关注过对方
        Fans fans = fansRepository.findOne(new FansPKeys(myUid, hisUid));
        if (fans != null)
            throw new ActionNotSupportException("操作不可用，Resource [UID: " + myUid + "]已经关注过对方了！Resource [UID: " + hisUid + "]");
        // 查看对方有没有关注自己
        fans = fansRepository.findOne(new FansPKeys(hisUid, myUid));
        if (fans != null && fans.getDeleteFlag() != Fans.DELETE_FLAG.delete) {
            // 关注了，表示互相关注
            Fans f = new Fans();
            f.setCreateTime(new Date());
            f.setDeleteFlag(Fans.DELETE_FLAG.show);
            f.setFansUid(myUid);
            f.setFollowedUid(hisUid);
            f.setFollowEach(Fans.FOLLOW_EACH.each);
            fansRepository.save(f);
            fans.setFollowEach(Fans.FOLLOW_EACH.each);
            fansRepository.save(fans);
            userInfoService.followPlusOne(myUid); // 自己的关注人数 +1
            userInfoService.fansPlusOne(hisUid);    // 他的粉丝数 +1
        } else {
            Fans f = new Fans();
            f.setCreateTime(new Date());
            f.setDeleteFlag(Fans.DELETE_FLAG.show);
            f.setFansUid(myUid);
            f.setFollowedUid(hisUid);
            f.setFollowEach(Fans.FOLLOW_EACH.single);
            fansRepository.save(f);
            userInfoService.followPlusOne(myUid); // 自己的关注人数 +1
            userInfoService.fansPlusOne(hisUid);    // 他的粉丝数 +1
        }
    }

    @Override
    public void cancelFollowSomeone(Long myUid, Long herUid) throws NoSuchUserException, ActionNotSupportException {
        // 有这个人？
        if (!userReactService.exists(herUid))
            throw new NoSuchUserException(herUid);
        // 是否关注过
        Fans fans = fansRepository.findOne(new FansPKeys(myUid, herUid));
        if (fans == null)
            throw new ActionNotSupportException("操作不可用，Resource [UID: " + myUid + "]还未关注对方！Resource [UID: " + herUid + "]");
        // 查看对方有没有关注自己
        Fans herFans = fansRepository.findOne(new FansPKeys(herUid, myUid));
        if (herFans != null && herFans.getDeleteFlag() != Fans.DELETE_FLAG.delete) {
            // 关注了，表示互相关注
            // 对方设置为单向关注
            herFans.setFollowEach(Fans.FOLLOW_EACH.single);
            fansRepository.save(herFans);
            // 己方删除记录
            fansRepository.delete(fans);
            userInfoService.followMinsOne(myUid); // 自己的关注人数 -1
            userInfoService.fansMinsOne(herUid);    // 他的粉丝数 -1
        } else {
            // 己方删除记录
            fansRepository.delete(fans);
            userInfoService.followMinsOne(myUid); // 自己的关注人数 -1
            userInfoService.fansMinsOne(herUid);    // 他的粉丝数 -1
        }
    }

    // 找到自己的粉丝们
    @Override
    public Page findPageByFollowedUid(Long followedUid, Pageable pageable) {
        Page<Object[]> page = fansRepository.findItByFollowedUid(followedUid, pageable);
        return new PageImpl(transferToFansVOIgnoreFollowedUid(page.getContent()), pageable, page.getTotalElements());
    }

    @Override
    public Page findPageByFollowedUidWithFullIdentificationInfo(Long followedUid, Pageable pageable) {
        Page<Object[]> page = fansRepository.findItByFollowedUidWithFullIdentificationInfo(followedUid, pageable);
        return new PageImpl(transferToFansVOIgnoreFollowedUidWithFullIdentificationInfo(page.getContent()), pageable, page.getTotalElements());
    }

    @Override
    public List<Fans> findAllByFansUid(Long uid) {
        return fansRepository.findAllByFansUid(uid);
    }

    private List<FansVO> transferToFansVOIgnoreFansUidWithFullIdentificationInfo(List<Object[]> objectsList) {
        List<FansVO> fansVOS = new ArrayList<>();
        for (int i = 0; i < objectsList.size(); i++) {
            Object[] objects = objectsList.get(i);
            if (objects == null || objects.length != 8) continue;// 数据长度不一致
            FansVO vo = new FansVO();
//            vo.setFansUid(parseLong(objects[0]));
            vo.setFollowedUid(parseLong(objects[1]));
            vo.setFollowEach(parseEnumFOLLOW_EACH(objects[2]));
            vo.setNickname(parseString(objects[3]));
            vo.setHeadIconUrl(parseString(objects[4]));
            vo.setIntroduce(parseString(objects[5]));
            vo.setIdentificationIds(null); // 必须为空，否则 VO 显示就会多余数据
            vo.setIdentifications(parseToFansIdentificationVOs(objects[6], objects[7]));
            fansVOS.add(vo);
        }
        return fansVOS;
    }

    private List<FansVO> transferToFansVOIgnoreFansUid(List<Object[]> objectsList) {
        List<FansVO> fansVOS = new ArrayList<>();
        for (int i = 0; i < objectsList.size(); i++) {
            Object[] objects = objectsList.get(i);
            if (objects == null || objects.length != 7) continue;// 数据长度不一致
            FansVO vo = new FansVO();
//            vo.setFansUid(parseLong(objects[0]));
            vo.setFollowedUid(parseLong(objects[1]));
            vo.setFollowEach(parseEnumFOLLOW_EACH(objects[2]));
            vo.setNickname(parseString(objects[3]));
            vo.setHeadIconUrl(parseString(objects[4]));
            vo.setIntroduce(parseString(objects[5]));
            vo.setIdentificationIds(parseBytesToLongList(objects[6]));
            vo.setIdentifications(null);
            fansVOS.add(vo);
        }
        return fansVOS;
    }

    private List<FansVO> transferToFansVOIgnoreFollowedUidWithFullIdentificationInfo(List<Object[]> objectsList) {
        List<FansVO> fansVOS = new ArrayList<>();
        for (int i = 0; i < objectsList.size(); i++) {
            Object[] objects = objectsList.get(i);
            if (objects == null || objects.length != 8) continue;// 数据长度不一致
            FansVO vo = new FansVO();
            vo.setFansUid(parseLong(objects[0]));
//            vo.setFollowedUid(parseLong(objects[1]));
            vo.setFollowEach(parseEnumFOLLOW_EACH(objects[2]));
            vo.setNickname(parseString(objects[3]));
            vo.setHeadIconUrl(parseString(objects[4]));
            vo.setIntroduce(parseString(objects[5]));
            vo.setIdentificationIds(null); // 必须为空，否则 VO 显示就会多余数据
            vo.setIdentifications(parseToFansIdentificationVOs(objects[6], objects[7]));
            fansVOS.add(vo);
        }
        return fansVOS;
    }

    private List<FansVO> transferToFansVOIgnoreFollowedUid(List<Object[]> objectsList) {
        List<FansVO> fansVOS = new ArrayList<>();
        for (int i = 0; i < objectsList.size(); i++) {
            Object[] objects = objectsList.get(i);
            if (objects == null || objects.length != 7) continue;// 数据长度不一致
            FansVO vo = new FansVO();
            vo.setFansUid(parseLong(objects[0]));
//            vo.setFollowedUid(parseLong(objects[1]));
            vo.setFollowEach(parseEnumFOLLOW_EACH(objects[2]));
            vo.setNickname(parseString(objects[3]));
            vo.setHeadIconUrl(parseString(objects[4]));
            vo.setIntroduce(parseString(objects[5]));
            vo.setIdentificationIds(parseBytesToLongList(objects[6]));
            vo.setIdentifications(null);
            fansVOS.add(vo);
        }
        return fansVOS;
    }

    //// parse

    private List<FansIdentificationVO> parseToFansIdentificationVOs(Object ids, Object urls) {
        if (ids == null || urls == null) return new ArrayList<>();
        String[] idsStrs = String.valueOf(ids).split(",");
        String[] urlsStrs = String.valueOf(urls).split("@@");
        if (idsStrs == null || urlsStrs == null || idsStrs.length != urlsStrs.length) return new ArrayList<>();
        List<FansIdentificationVO> vos = new ArrayList<>();
        for (int i = 0; i < idsStrs.length; i++) {
            FansIdentificationVO vo = new FansIdentificationVO();
            vo.setIdentificationId(parseLong(idsStrs[i]));
            vo.setIconActiveUrl(urlsStrs[i]);
            vos.add(vo);
        }
        return vos;
    }

    private List<Long> parseBytesToLongList(Object object) {
        if (object == null) return new ArrayList<>();
        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            if (bytes.length == 0) return new ArrayList<>();
            List<Long> longs = new ArrayList<>();
            String temp = "";
            for (int i = 0; i < bytes.length; i++) {
                byte id = bytes[i];
                if (id == 44) {
                    // 这是逗号
                    longs.add(parseLong(temp)); // 转成对应的 int 值
                    temp = "";
                } else if (i + 1 == bytes.length) {
                    // 这是句末
                    temp += Byte.toUnsignedLong(id) - 48;
                    longs.add(parseLong(temp));
                    temp = "";
                } else {
                    temp += Byte.toUnsignedLong(id) - 48;
                }
            }
        }
        if (object instanceof String) {
            String longStr = object.toString();
            List<Long> longs = new ArrayList<>();
            String[] ids = longStr.split(",");
            for (String id : ids) {
                longs.add(parseLong(id));
            }
            return longs;
        }
        return new ArrayList<>();
    }

    private Long parseLong(Object object) {
        if (object == null) return null;
        return Long.parseLong(object.toString());
    }

    private String parseString(Object object) {
        if (object == null) return null;
        return object.toString();
    }

    private Fans.FOLLOW_EACH parseEnumFOLLOW_EACH(Object object) {
        if (object == null) return Fans.FOLLOW_EACH.none;
        int value = Integer.parseInt(object.toString());
        if (value == 1) return Fans.FOLLOW_EACH.single;
        if (value == 2) return Fans.FOLLOW_EACH.each;
        return Fans.FOLLOW_EACH.none;
    }
}
