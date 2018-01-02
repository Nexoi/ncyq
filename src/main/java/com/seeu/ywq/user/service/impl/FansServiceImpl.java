package com.seeu.ywq.user.service.impl;

import com.seeu.ywq.user.dvo.FansVO;
import com.seeu.ywq.user.model.Fans;
import com.seeu.ywq.user.model.FansPKeys;
import com.seeu.ywq.user.repository.FansRepository;
import com.seeu.ywq.user.service.FansService;
import com.seeu.ywq.userlogin.service.UserReactService;
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

    @Override
    public boolean hasFollowedHer(Long myUid, Long herUid) {
        return fansRepository.exists(new FansPKeys(myUid, herUid));
    }

    @Override
    public Page findPageByFansUid(Long fansUid, Pageable pageable) {
        Page<Object[]> page = fansRepository.findItByFansUid(fansUid, pageable);
        List<FansVO> fansVOS = new ArrayList<>();
        List<Object[]> objectsList = page.getContent();
        for (int i = 0; i < objectsList.size(); i++) {
            Object[] objects = objectsList.get(i);
            FansVO vo = new FansVO();
//            vo.setFansUid(parseLong(objects[0]));
            vo.setFollowedUid(parseLong(objects[1]));
            vo.setFollowEach(parseEnumFOLLOW_EACH(objects[2]));
            vo.setNickname(parseString(objects[3]));
            vo.setHeadIconUrl(parseString(objects[4]));
            vo.setText(parseString(objects[5]));
            vo.setIdentificationIds(parseBytesToLongList(objects[6]));
            fansVOS.add(vo);
        }
        return new PageImpl(fansVOS, pageable, page.getTotalElements());
    }

    @Override
    public STATUS followSomeone(Long myUid, Long hisUid) {
        // 有这个人？
        if (!userReactService.exists(hisUid))
            return STATUS.not_such_person;
        // 先查看自己是否已经关注过对方
        Fans fans = fansRepository.findOne(new FansPKeys(myUid, hisUid));
        if (fans != null)
            return STATUS.have_followed;
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
            return STATUS.success;
        } else {
            Fans f = new Fans();
            f.setCreateTime(new Date());
            f.setDeleteFlag(Fans.DELETE_FLAG.show);
            f.setFansUid(myUid);
            f.setFollowedUid(hisUid);
            f.setFollowEach(Fans.FOLLOW_EACH.single);
            fansRepository.save(f);
            return STATUS.success;
        }
    }

    @Override
    public Page findPageByFollowedUid(Long followedUid, Pageable pageable) {
        Page<Object[]> page = fansRepository.findItByFollowedUid(followedUid, pageable);
        List<FansVO> fansVOS = new ArrayList<>();
        List<Object[]> objectsList = page.getContent();
        for (int i = 0; i < objectsList.size(); i++) {
            Object[] objects = objectsList.get(i);
            FansVO vo = new FansVO();
            vo.setFansUid(parseLong(objects[0]));
//            vo.setFollowedUid(parseLong(objects[1]));
            vo.setFollowEach(parseEnumFOLLOW_EACH(objects[2]));
            vo.setNickname(parseString(objects[3]));
            vo.setHeadIconUrl(parseString(objects[4]));
            vo.setText(parseString(objects[5]));
            vo.setIdentificationIds(parseBytesToLongList(objects[6]));
            fansVOS.add(vo);
        }
        return new PageImpl(fansVOS, pageable, page.getTotalElements());
    }

    @Override
    public List<Fans> findAllByFansUid(Long uid) {
        return fansRepository.findAllByFansUid(uid);
    }

    private List<Long> parseBytesToLongList(Object object) {
        if (object == null) return new ArrayList<>();
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
        return longs;
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
