package com.seeu.ywq.api.release;

import com.seeu.ywq.release.service.FansService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/test")
public class ApiTest {
    @Resource
    private FansService fansService;

    @GetMapping("/{uid}")
    public ResponseEntity get(@PathVariable Long uid) {
        return ResponseEntity.ok(fansService.findPageByFansUid(uid, new PageRequest(0, 10)));
    }
}
