package com.seeu.ywq.release.service.apppage.impl;

import com.seeu.ywq.release.dvo.apppage.PageBalance;
import com.seeu.ywq.release.repository.apppage.PageBalanceRepository;
import com.seeu.ywq.release.service.apppage.AppVOService;
import com.seeu.ywq.release.service.apppage.PageBalanceRankingService;
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
