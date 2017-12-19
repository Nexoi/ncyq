package com.seeu.ywq.release.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface AppPageNearByPeopleService {
    // 附近的人
    Page findNearMe(BigDecimal longitude, BigDecimal latitude, Pageable pageable);
}
