package com.seeu.ywq.api.release.page;

import com.seeu.ywq.page.model.HomePageCategory;
import com.seeu.ywq.page.service.AppHomePageService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "APP页面数据网红主页接口", description = "网红")
@RestController
@RequestMapping("/api/v1/page")
public class HomePageWanghongApi {
    @Autowired
    private AppHomePageService appHomePageService;

    @ApiOperation(value = "网红（固定长度）")
    @GetMapping("/hot-person")
    public ResponseEntity hotPerson(@AuthenticationPrincipal UserLogin authUser) {
        List<HomePageCategory> categoryList = null;
        if (authUser == null)
            categoryList = appHomePageService.queryAllByPage(HomePageCategory.PAGE.hotsperson);
        else
            categoryList = appHomePageService.queryAllByPage(authUser.getUid(), HomePageCategory.PAGE.hotsperson);
        return ResponseEntity.ok(categoryList);
//        Map map = new HashMap();
//        map.put("news", appHomePageService.getHotsPerson_New());
//        map.put("suggestion", appHomePageService.getHotsPerson_Suggestion());
//        return ResponseEntity.ok(map);
    }

}