package com.seeu.ywq.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.seeu.ywq.message.model.PersonalMessage;
import com.seeu.ywq.message.repository.PersonalMessageRepository;
import com.seeu.ywq.message.service.PersonalMessageService;
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
public class PersonalMessageServiceImpl implements PersonalMessageService {

    @Resource
    private PersonalMessageRepository repository;

    @Override
    public Page<PersonalMessage> findAll(Long uid, Pageable pageable) {
        Page page = repository.findAllByUid(uid, pageable);
        List<PersonalMessage> list = page.getContent();
        for (PersonalMessage message : list) {
            if (message == null) continue;
            message.setJson(JSON.parseObject(message.getExtraJson()));
            message.setExtraJson(null); // 清理掉不必要的数据
        }
        return page;
    }

    @Override
    public Page<PersonalMessage> findAll(Long uid, PersonalMessage.TYPE type, Pageable pageable) {
        Page page = null;
        if (type == PersonalMessage.TYPE.others)
            page = repository.findAllByUidAndTypeNot(uid, PersonalMessage.TYPE.like_comment, pageable);
        else
            page = repository.findAllByUidAndType(uid, type, pageable);
        List<PersonalMessage> list = page.getContent();
        for (PersonalMessage message : list) {
            if (message == null) continue;
            message.setJson(JSON.parseObject(message.getExtraJson()));
            message.setExtraJson(null); // 清理掉不必要的数据
        }
        return page;
    }

    @Override
    public List<PersonalMessage> findMine(Long uid, PersonalMessage.TYPE type, Date date) {
        List<PersonalMessage> list = null;
        if (type == PersonalMessage.TYPE.others)
            list = repository.findAllByUidAndTypeNotAndCreateTimeAfter(uid, PersonalMessage.TYPE.like_comment, date);
        else
            list = repository.findAllByUidAndTypeAndCreateTimeAfter(uid, type, date);
        for (PersonalMessage message : list) {
            if (message == null) continue;
            message.setJson(JSON.parseObject(message.getExtraJson()));
            message.setExtraJson(null); // 清理掉不必要的数据
        }
        return list;
    }

    @Override
    public Integer countLikeComment(Long uid, Date date) {
        return repository.countAllByUidAndTypeAndCreateTimeAfter(uid, PersonalMessage.TYPE.like_comment, date);
    }

    @Override
    public Integer countOthers(Long uid, Date date) {
        return repository.countAllByUidAndTypeNotAndCreateTimeAfter(uid, PersonalMessage.TYPE.like_comment, date);
    }

    @Override
    public PersonalMessage add(PersonalMessage message) {
        if (message == null) return null;
        return repository.save(message);
    }

}
