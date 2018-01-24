package com.seeu.ywq.api.admin.gift;

import com.seeu.core.R;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.gift.model.Gift;
import com.seeu.ywq.gift.service.GiftService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 22/01/2018
 * Time: 6:09 PM
 * Describe:
 */


@Api(tags = "配置-在线送礼/打赏礼物配置", description = "送礼礼物列表")
@RestController("adminGiftApi")
@RequestMapping("/api/admin/v1/gift")
public class GiftApi {
    @Autowired
    private GiftService giftService;

    // TODO
    @GetMapping("/list")
    public Page<Gift> list(@RequestParam(defaultValue = "0") Integer page,
                           @RequestParam(defaultValue = "10") Integer size) {
        return giftService.findAll(new PageRequest(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        try {
            Gift gift = giftService.findOne(id);
            return ResponseEntity.ok(gift);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该资源"));
        }
    }

    @PostMapping
    public Gift add(@Validated Gift gift) {
        return giftService.save(gift);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Validated Gift gift) {
        gift.setId(id);
        try {
            return ResponseEntity.ok(giftService.update(gift));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该资源"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        giftService.delete(id);
        return ResponseEntity.ok(R.code(200).message("删除成功"));
    }
}
