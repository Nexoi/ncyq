package com.seeu.ywq.page.service;

import com.seeu.ywq.page.dvo.PageBalance;

import java.util.List;

public interface PageBalanceRankingService {
    List<PageBalance> getRanking(Integer size);
}
