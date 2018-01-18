package com.seeu.ywq.ranking.service.impl;

import com.seeu.ywq.ranking.dvo.PageBalance;
import com.seeu.ywq.ranking.repository.PageBalanceRepository;
import com.seeu.ywq.ranking.service.PageBalanceRankingService;
import com.seeu.ywq.utils.AppVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PageBalanceRankingServiceImpm implements PageBalanceRankingService {
    @Resource
    private PageBalanceRepository repository;
    @Autowired
    private AppVOUtils appVOUtils;

    @Override
    public List<PageBalance> getRanking(Integer size) {
        List<Object[]> list = repository.queryItTop1X(size);
        List<PageBalance> pageBalances = formPageBalance(list);
        return pageBalances;
    }

    private PageBalance formPageBalance(Object[] objects) {
        if (objects == null || objects.length != 4) return null;// 长度必须是 4 个
        PageBalance pb = new PageBalance();
        pb.setUid(appVOUtils.parseLong(objects[0]));
        pb.setBalance(appVOUtils.parseLong(objects[1]));
        pb.setNickname(appVOUtils.parseString(objects[2]));
        pb.setHeadIconUrl(appVOUtils.parseString(objects[3]));
        return pb;
    }

    private List<PageBalance> formPageBalance(List<Object[]> objects) {
        if (objects == null || objects.size() == 0) return new ArrayList<>();
        List<PageBalance> list = new ArrayList();
        for (Object[] object : objects) {
            list.add(formPageBalance(object));
        }
        return list;
    }
}
