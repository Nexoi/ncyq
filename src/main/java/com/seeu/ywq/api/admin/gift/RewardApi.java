package com.seeu.ywq.api.admin.gift;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.gift.model.Reward;
import com.seeu.ywq.gift.service.RewardService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 22/01/2018
 * Time: 6:09 PM
 * Describe:
 */


@Api(tags = "配置-在线送礼/打赏礼物配置", description = "打赏礼物列表")
@RestController("adminRewardApi")
@RequestMapping("/api/admin/v1/reward")
@PreAuthorize("hasRole('ADMIN')")
public class RewardApi {
    @Autowired
    private RewardService rewardService;

    // TODO
    @GetMapping("/list")
    public Page<Reward> list(@RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "10") Integer size) {
        return rewardService.findAll(new PageRequest(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        try {
            Reward reward = rewardService.findOne(id);
            return ResponseEntity.ok(reward);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该资源"));
        }
    }

    @PostMapping
    public Reward add(@Validated Reward reward) {
        return rewardService.save(reward);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Validated Reward reward) {
        reward.setId(id);
        try {
            return ResponseEntity.ok(rewardService.update(reward));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该资源"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        rewardService.delete(id);
        return ResponseEntity.ok(R.code(200).message("删除成功"));
    }
}
