package com.seeu.ywq.api.release;

import com.seeu.ywq.release.model.apppage.PageConfig;
import com.seeu.ywq.release.repository.apppage.PageConfigRepository;
import com.seeu.ywq.release.service.FansService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class ApiTest {
    @Resource
    private FansService fansService;

    @Resource
    private PageConfigRepository pageConfigRepository;

    @GetMapping
    public ResponseEntity get(@RequestParam PageConfig.CATEGORY category) {
        List list = pageConfigRepository.findXXByCategoryFromConfig(category.ordinal());
        return ResponseEntity.ok(list);
    }
}
