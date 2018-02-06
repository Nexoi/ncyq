package com.seeu.ywq.api.admin.activity;

import com.seeu.core.R;
import com.seeu.ywq._web.model.WebPageActivity;
import com.seeu.ywq._web.service.WebPageActivityService;
import com.seeu.ywq.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 06/02/2018
 * Time: 3:52 PM
 * Describe:
 */

@Api(tags = "活动页面 H5 编辑", description = "活动")
@RestController
@RequestMapping("/api/admin/v1/activity")
public class ActivityPageApi {

    @Autowired
    private WebPageActivityService webPageActivityService;

    @GetMapping("/list")
    public Page<WebPageActivity> list(@RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return webPageActivityService.findAll(new PageRequest(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(webPageActivityService.findOne(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该页面"));
        }
    }

    @PostMapping
    public WebPageActivity add(String title, String htmlContent) {
        return webPageActivityService.save(title, htmlContent);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @RequestParam String title,
                                 @RequestParam String htmlContent) {
        try {
            return ResponseEntity.ok(webPageActivityService.update(id, title, htmlContent));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(R.code(404).message("找不到该页面"));
        }
    }

}
