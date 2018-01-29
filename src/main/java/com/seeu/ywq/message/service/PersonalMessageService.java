package com.seeu.ywq.message.service;

import com.seeu.ywq.message.model.PersonalMessage;
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

public interface PersonalMessageService {

    // suggestion: 按时间排序
    Page<PersonalMessage> findAll(Long uid, Pageable pageable);

    Page<PersonalMessage> findAll(Long uid, PersonalMessage.TYPE type, Pageable pageable);

    List<PersonalMessage> findMine(Long uid, PersonalMessage.TYPE type, Date date);

    Integer countLikeComment(Long uid, Date date);

    Integer countOthers(Long uid, Date date);

    PersonalMessage add(PersonalMessage message);
}
