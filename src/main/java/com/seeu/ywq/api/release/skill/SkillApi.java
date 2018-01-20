package com.seeu.ywq.api.release.skill;

import com.seeu.ywq.skill.service.SkillService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 6:32 PM
 * Describe:
 */
@Api(tags = {"技能"}, description = "列出所有技能", position = 10)
@RestController
@RequestMapping("/api/v1/skill")
public class SkillApi {
    @Autowired
    private SkillService skillService;

    @GetMapping("/list")
    public ResponseEntity list() {
        return ResponseEntity.ok(skillService.findAll());
    }
}
