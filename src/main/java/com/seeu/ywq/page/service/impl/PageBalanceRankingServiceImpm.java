package com.seeu.ywq.page.service.impl;

import com.seeu.ywq.page.dvo.PageBalance;
import com.seeu.ywq.page.repository.PageBalanceRepository;
import com.seeu.ywq.page.service.AppVOService;
import com.seeu.ywq.page.service.PageBalanceRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PageBalanceRankingServiceImpm implements PageBalanceRankingService {
    @Resource
    private PageBalanceRepository repository;
    @Autowired
    private AppVOService appVOService;

    @Override
    public List<PageBalance> getRanking(Integer size) {
        List<Object[]> list = repository.queryItTop1X(size);
        List<PageBalance> pageBalances = appVOService.formPageBalance(list);
        return pageBalances;
    }
}
