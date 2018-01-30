package com.seeu.ywq.api.release.search;

import com.seeu.ywq.search.model.HotWord;
import com.seeu.ywq.search.service.HotWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by suneo.
 * User: neo
 * Date: 30/01/2018
 * Time: 3:29 PM
 * Describe:
 */


@Api(tags = "搜索", description = "搜索功能")
@RestController
@RequestMapping("/api/v1/search")
public class SearchApi {
    @Autowired
    private HotWordService hotWordService;

    @ApiOperation("获取热搜关键词（最新时间排序）")
    @GetMapping("/words")
    public Page<HotWord> listWords(@RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        return hotWordService.findAll(new PageRequest(page, size));
    }
}
