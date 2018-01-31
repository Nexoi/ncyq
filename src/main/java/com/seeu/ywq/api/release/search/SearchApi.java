package com.seeu.ywq.api.release.search;

import com.seeu.ywq.search.model.HotWord;
import com.seeu.ywq.search.service.HotWordService;
import com.seeu.ywq.trend_lite.dvo.PublishLiteVO;
import com.seeu.ywq.trend_lite.service.PublishLiteService;
import com.seeu.ywq.user.dvo.SimpleUserVO;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.UserReactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @Autowired
    private UserReactService userReactService;
    @Autowired
    private PublishLiteService publishLiteService;

    @ApiOperation("获取热搜关键词（最新时间排序）")
    @GetMapping("/words")
    public Page<HotWord> listWords(@RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        return hotWordService.findAll(new PageRequest(page, size));
    }


    @ApiOperation("搜索用户")
    @GetMapping("/users")
    public Page<SimpleUserVO> searchUsers(@AuthenticationPrincipal UserLogin authUser,
                                          @RequestParam String word,
                                          @RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        Long uid = null;
        if (authUser != null)
            uid = authUser.getUid();
        return userReactService.search(uid, word, new PageRequest(page, size));
    }

    @ApiOperation("搜索动态")
    @GetMapping("/publishs")
    public Page<PublishLiteVO> searchPublish(@AuthenticationPrincipal UserLogin authUser,
                                             @RequestParam String word,
                                             @RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size) {
        Long uid = null;
        if (authUser != null)
            uid = authUser.getUid();
        return publishLiteService.search(uid, word, new PageRequest(page, size));
    }

}
