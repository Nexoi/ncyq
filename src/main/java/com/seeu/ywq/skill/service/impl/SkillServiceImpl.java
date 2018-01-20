package com.seeu.ywq.skill.service.impl;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.skill.model.Skill;
import com.seeu.ywq.skill.repository.SkillRepository;
import com.seeu.ywq.skill.service.SkillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 6:27 PM
 * Describe:
 */
@Service
public class SkillServiceImpl implements SkillService {
    @Resource
    private SkillRepository repository;

    @Override
    public Skill findOne(Long id) throws ResourceNotFoundException {
        if (id == null) throw new ResourceNotFoundException("无此技能配置");
        Skill skill = repository.findOne(id);
        if (skill == null) throw new ResourceNotFoundException("无此技能配置");
        return skill;
    }

    @Override
    public List<Skill> findAll() {
        return repository.findAll();
    }

    @Override
    public Skill save(String skillName) {
        Skill skill = new Skill();
        skill.setSkillName(skillName);
        return repository.save(skill);
    }

    @Override
    public Skill update(Long id, String skillName) throws ResourceNotFoundException {
        Skill skill = findOne(id);
        skill.setSkillName(skillName);
        return repository.save(skill);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Skill skill = findOne(id);
        repository.delete(skill);
    }
}
