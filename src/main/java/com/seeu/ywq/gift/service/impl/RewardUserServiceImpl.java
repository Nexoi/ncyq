package com.seeu.ywq.gift.service.impl;

import com.seeu.ywq.gift.dvo.RewardUserVO;
import com.seeu.ywq.gift.model.RewardOrder;
import com.seeu.ywq.gift.service.RewardOrderService;
import com.seeu.ywq.gift.service.RewardUserService;
import com.seeu.ywq.page.dvo.SimpleUserVO;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardUserServiceImpl implements RewardUserService {
    @Autowired
    private RewardOrderService rewardOrderService;

    @Autowired
    private UserReactService userReactService;


    @Override
    public Page<RewardUserVO> findAll(Long uid, Pageable pageable) {
        Page<RewardOrder> rewardOrders = rewardOrderService.findAllByUid(uid, pageable);
        List<RewardOrder> list = rewardOrders.getContent();
        if (list == null || list.size() == 0) return new PageImpl<RewardUserVO>(new ArrayList<>());

        // search for bind user vo
        List<Long> ids = new ArrayList<>();
        List<RewardUserVO> vos = new ArrayList<>();
        Map<Long, RewardUserVO> map = new HashMap<>();
        for (RewardOrder order : list) {
            ids.add(order.getHerUid());
            RewardUserVO vo = new RewardUserVO();
            vo.setCreateTime(order.getCreateTime());
            vo.setDiamonds(order.getDiamonds());
            vos.add(vo);
            map.put(order.getHerUid(), vo);
        }
        // bind
        if (ids.size() == 0) return new PageImpl<>(new ArrayList<>());
        List<SimpleUserVO> userList = userReactService.findAllSimpleUsers(uid, ids);
        for (SimpleUserVO user : userList) {
            RewardUserVO vo = map.get(user.getUid());
            if (vo != null)
                vo.setUser(user);
        }
        return new PageImpl<RewardUserVO>(vos, pageable, rewardOrders.getTotalElements());
    }
}
