package com.seeu.ywq.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.seeu.ywq.message.model.SysMessage;
import com.seeu.ywq.message.repository.SysMessageRepository;
import com.seeu.ywq.message.service.SysMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 26/01/2018
 * Time: 5:01 PM
 * Describe:
 */
@Service
public class SystemMessageServiceImpl implements SysMessageService {

    @Resource
    private SysMessageRepository repository;

    @Override
    public Page<SysMessage> findAll(Pageable pageable) {
        Page page = repository.findAll(pageable);
        List<SysMessage> list = page.getContent();
        for (SysMessage message : list) {
            if (message == null) continue;
            message.setJson(JSON.parseObject(message.getExtraJson()));
            message.setExtraJson(null); // 清理掉不必要的数据
        }
        return page;
    }

    @Override
    public List<SysMessage> findByDate(Date date) {
        List<SysMessage> list = repository.findAllByCreateTimeAfter(date);
        for (SysMessage message : list) {
            if (message == null) continue;
            message.setJson(JSON.parseObject(message.getExtraJson()));
            message.setExtraJson(null); // 清理掉不必要的数据
        }
        return list;
    }

    @Override
    public SysMessage add(SysMessage message) {
        if (message == null) return null;
        return repository.save(message);
    }
}
