package com.seeu.ywq.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.seeu.ywq.message.model.PersonalMessage;
import com.seeu.ywq.message.repository.PersonalMessageRepository;
import com.seeu.ywq.message.service.PersonalMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

//    @Override
//    public Page<PersonalMessage> findAll(Long uid, PersonalMessage.TYPE type, Pageable pageable) {
//        Page page = null;
//        if (type == PersonalMessage.TYPE.others)
//            page = repository.findAllByUidAndTypeNot(uid, PersonalMessage.TYPE.like, pageable);
//        else
//            page = repository.findAllByUidAndType(uid, type, pageable);
//        List<PersonalMessage> list = page.getContent();
//        for (PersonalMessage message : list) {
//            if (message == null) continue;
//            message.setJson(JSON.parseObject(message.getExtraJson()));
//            message.setExtraJson(null); // 清理掉不必要的数据
//        }
//        return page;
//    }
//
//    @Override
//    public List<PersonalMessage> findMine(Long uid, PersonalMessage.TYPE type, Date date) {
//        List<PersonalMessage> list = null;
//        if (type == PersonalMessage.TYPE.others)
//            list = repository.findAllByUidAndTypeNotAndCreateTimeAfter(uid, PersonalMessage.TYPE.like, date);
//        else
//            list = repository.findAllByUidAndTypeAndCreateTimeAfter(uid, type, date);
//        for (PersonalMessage message : list) {
//            if (message == null) continue;
//            message.setJson(JSON.parseObject(message.getExtraJson()));
//            message.setExtraJson(null); // 清理掉不必要的数据
//        }
//        return list;
//    }

    /**
     * 综合方法，按类组合查询所有消息
     *
     * @param uid
     * @param types
     * @param date
     * @return
     */
    @Override
    public List<PersonalMessage> findAll(Long uid, Collection<PersonalMessage.TYPE> types, Date date) {
        return repository.findAllByUidAndTypeInAndCreateTimeAfterOrderByCreateTimeDesc(uid, types, date);
    }

    @Override
    public List<PersonalMessage> findAll(Long uid, Collection<PersonalMessage.TYPE> types, Date date, Integer number) {
        PageRequest request = new PageRequest(0, number, new Sort(Sort.Direction.DESC, "createTime"));
        Page page = repository.findAllByUidAndTypeInAndCreateTimeBefore(uid, types, date, request);
        return page.getContent();
    }

    @Override
    public Map<PersonalMessage.TYPE, Integer> countAll(Long uid, Date date) {
        List<PersonalMessage.TYPE> types = new ArrayList<>();
        types.add(PersonalMessage.TYPE.like);
        types.add(PersonalMessage.TYPE.comment);
        types.add(PersonalMessage.TYPE.gift);
        types.add(PersonalMessage.TYPE.reward);
        types.add(PersonalMessage.TYPE.yellowPicture);
        Map<PersonalMessage.TYPE, Integer> map = new HashMap<>();
        map.put(PersonalMessage.TYPE.like, 0);
        map.put(PersonalMessage.TYPE.comment, 0);
        map.put(PersonalMessage.TYPE.gift, 0);
        map.put(PersonalMessage.TYPE.reward, 0);
        map.put(PersonalMessage.TYPE.yellowPicture, 0);
        List<PersonalMessage> messages = findAll(uid, types, date);
        for (PersonalMessage message : messages) {
            if (message == null || message.getType() == null) continue;
            int sum = map.get(message.getType()).intValue() + 1;
            map.put(message.getType(), sum);
        }
        return map;
    }

//    @Override
//    public Integer countLikeComment(Long uid, Date date) {
//        return repository.countAllByUidAndTypeAndCreateTimeAfter(uid, PersonalMessage.TYPE.like, date);
//    }
//
//    @Override
//    public Integer countOthers(Long uid, Date date) {
//        return repository.countAllByUidAndTypeNotAndCreateTimeAfter(uid, PersonalMessage.TYPE.like, date);
//    }

    @Override
    public PersonalMessage add(PersonalMessage message) {
        if (message == null) return null;
        return repository.save(message);
    }

}
