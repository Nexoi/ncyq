package com.seeu.ywq.skill.service.impl;

import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.skill.model.UserSkillPrice;
import com.seeu.ywq.skill.repository.UserSkillPriceRepository;
import com.seeu.ywq.skill.service.UserSkillPriceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 6:52 PM
 * Describe:
 */

@Service
public class UserSkillPriceServiceImpl implements UserSkillPriceService {
    @Resource
    private UserSkillPriceRepository repository;

    @Override
    public UserSkillPrice findOne(Long uid) throws ResourceNotFoundException {
        if (uid == null) throw new ResourceNotFoundException("找不到该资源");
        UserSkillPrice price = repository.findOne(uid);
        if (price == null)
            throw new ResourceNotFoundException("找不到该资源");
        return price;
    }

    @Override
    public UserSkillPrice save(UserSkillPrice userSkill) throws ActionParameterException {
        if (userSkill == null || userSkill.getUid() == null || userSkill.getPrice() == null || userSkill.getPrice() < 0)
            throw new ActionParameterException("参数不能为空");
        return repository.save(userSkill);
    }
}
