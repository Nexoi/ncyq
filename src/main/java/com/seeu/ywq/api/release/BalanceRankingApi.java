package com.seeu.ywq.api.release;

import com.seeu.ywq.page.service.PageBalanceRankingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "榜单", description = "财富榜")
@RestController
@RequestMapping("/api/v1/ranking")
public class BalanceRankingApi {
    @Autowired
    private PageBalanceRankingService pageBalanceRankingService;

    @ApiOperation(value = "财富榜")
    @GetMapping("/balance")
    public ResponseEntity rankingBalance(@RequestParam(defaultValue = "12") Integer size) {
        return ResponseEntity.ok(pageBalanceRankingService.getRanking(size));
    }
}
