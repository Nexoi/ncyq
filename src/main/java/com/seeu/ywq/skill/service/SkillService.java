package com.seeu.ywq.skill.service;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.skill.model.Skill;

import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 6:26 PM
 * Describe:
 */

public interface SkillService {
    Skill findOne(Long id) throws ResourceNotFoundException;

    List<Skill> findAll();

    Skill save(String skillName);

    Skill update(Long id, String skillName) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;
}
