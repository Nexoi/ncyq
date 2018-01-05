package com.seeu.ywq.api.release;

import com.seeu.ywq.page.repository.PublishLiteRepository;
import com.seeu.ywq.page.service.AppHomePageService;
import com.seeu.ywq.user.service.FansService;
import com.seeu.ywq.user.service.UserPositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class ApiTest {
    @Resource
    private FansService fansService;

    @Resource
    private AppHomePageService appPageService;

    @Resource
    private UserPositionService userPositionService;

    @Resource
    private PublishLiteRepository repository;

    @GetMapping
    public ResponseEntity get() {
//        List list = repository.queryItUseTags();
        return ResponseEntity.ok(null);
    }
}
