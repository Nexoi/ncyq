package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.skill.model.UserSkillPrice;
import com.seeu.ywq.skill.service.UserSkillPriceService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 6:33 PM
 * Describe:
 */

@Api(tags = {"用户技能"}, description = "配置价格", position = 10)
@RestController
@RequestMapping("/api/v1/user")
public class UserSkillApi {

    @Autowired
    private UserSkillPriceService userSkillPriceService;


    @ApiOperation("获取自己的技能价格")
    @GetMapping("/skill/price")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMyConfigurerPrice(@AuthenticationPrincipal UserLogin authUser) {
        try {
            return ResponseEntity.ok(userSkillPriceService.findOne(authUser.getUid()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(204).body(R.code(204).message("您还未设定价格"));
        }
    }


    @ApiOperation(value = "获取自己的技能价格", notes = "需要登录可见")
    @GetMapping("/{uid}/skill/price")
    @PreAuthorize("hasRole('USER')") // 默认需要已登陆用户才能看到
    public ResponseEntity getConfigurerPrice(@PathVariable Long uid) {
        try {
            return ResponseEntity.ok(userSkillPriceService.findOne(uid));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(204).body(R.code(204).message("该用户未设定价格"));
        }
    }

    @ApiOperation("设定技能价格（必须整数）")
    @PostMapping("/skill/price")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity configurerPrice(@AuthenticationPrincipal UserLogin authUser,
                                          Long price) {
        UserSkillPrice skillPrice = new UserSkillPrice();
        skillPrice.setUid(authUser.getUid());
        skillPrice.setPrice(price);
        try {
            return ResponseEntity.ok(userSkillPriceService.save(skillPrice));
        } catch (ActionParameterException e) {
            return ResponseEntity.status(400).body(R.code(400).message("参数设定异常"));
        }
    }
}
