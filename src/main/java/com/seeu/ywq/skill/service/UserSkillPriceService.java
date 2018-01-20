package com.seeu.ywq.skill.service;

import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.skill.model.UserSkillPrice;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 6:48 PM
 * Describe:
 */

public interface UserSkillPriceService {
    UserSkillPrice findOne(Long uid) throws ResourceNotFoundException;

    UserSkillPrice save(UserSkillPrice userSkill) throws ActionParameterException;
}
