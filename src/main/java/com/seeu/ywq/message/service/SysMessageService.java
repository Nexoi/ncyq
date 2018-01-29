package com.seeu.ywq.message.service;

import com.seeu.ywq.message.model.SysMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 26/01/2018
 * Time: 4:54 PM
 * Describe:
 */

public interface SysMessageService {

    // suggestion: 按时间排序
    Page<SysMessage> findAll(Pageable pageable);

    List<SysMessage> findByDate(Date date);

    Integer countMessages(Long uid, Date date);

    SysMessage add(SysMessage message);
}
