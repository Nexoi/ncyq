package com.seeu.ywq.api.admin.search;

import com.seeu.core.R;
import com.seeu.ywq.search.model.HotWord;
import com.seeu.ywq.search.service.HotWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 30/01/2018
 * Time: 3:29 PM
 * Describe:
 */


@Api(tags = "搜索关键词", description = "搜索关键词修改")
@RestController("adminSearchApi")
@RequestMapping("/api/admin/v1/hotword")
@PreAuthorize("hasRole('ADMIN')")
public class SearchApi {
    @Autowired
    private HotWordService hotWordService;

    @ApiOperation("获取热搜关键词（最新时间排序）")
    @GetMapping("/list")
    public Page<HotWord> listWords(@RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        return hotWordService.findAll(new PageRequest(page, size));
    }

    @PostMapping
    public HotWord add(String word) {
        return hotWordService.add(word);
    }

    @DeleteMapping
    public ResponseEntity delete(String word) {
        hotWordService.delete(word);
        return ResponseEntity.ok(R.code(200).message("删除成功"));
    }
}
