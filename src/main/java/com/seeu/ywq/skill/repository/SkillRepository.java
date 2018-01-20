package com.seeu.ywq.skill.repository;

import com.seeu.ywq.skill.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 6:25 PM
 * Describe:
 */

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
