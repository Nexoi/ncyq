package com.seeu.ywq.ranking.service;

import com.seeu.ywq.ranking.dvo.PageBalance;

import java.util.List;

public interface PageBalanceRankingService {
    List<PageBalance> getRanking(Integer size);
}
