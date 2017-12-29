package com.seeu.ywq.release.service.apppage;

import com.seeu.ywq.release.dvo.apppage.PageBalance;

import java.util.List;

public interface PageBalanceRankingService {
    List<PageBalance> getRanking(Integer size);
}
