package com.seeu.ywq.api.release.page;

import com.seeu.ywq.page.model.HomePageCategory;
import com.seeu.ywq.page.service.AppHomePageService;
import com.seeu.ywq.page_advertisement.model.Advertisement;
import com.seeu.ywq.page_advertisement.service.AdvertisementService;
import com.seeu.ywq.page_home.service.HomeUserService;
import com.seeu.ywq.user.service.UserPositionService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "APP页面数据主页接口", description = "首页四栏")
@RestController
@RequestMapping("/api/v1/page")
public class HomePageApi {
    @Autowired
    private AppHomePageService appHomePageService;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private UserPositionService userPositionService;
    @Autowired
    private HomeUserService homeUserService;

    @ApiOperation(value = "首页广告")
    @GetMapping("/homepage/advertisements")
    public ResponseEntity homePageAdvertisements() {
        return ResponseEntity.ok(advertisementService.getAdvertisements(Advertisement.CATEGORY.HomePage));
    }

    @ApiOperation(value = "首页固定长度数据")
    @GetMapping("/homepage")
    public ResponseEntity homePage1(@AuthenticationPrincipal UserLogin authUser) {
        List<HomePageCategory> categoryList = null;
        if (authUser == null)
            categoryList = appHomePageService.queryAllByPage(HomePageCategory.PAGE.home);
        else
            categoryList = appHomePageService.queryAllByPage(authUser.getUid(), HomePageCategory.PAGE.home);
        return ResponseEntity.ok(categoryList);
//        Map map = new HashMap();
//        map.put("newHotPerson", appHomePageService.getHomePage_NewHotsPerson());
//        map.put("newActor", appHomePageService.getHomePage_NewActors());
//        return ResponseEntity.ok(map);
    }


    @ApiOperation(value = "首页上拉加载数据")
    @GetMapping("/homepage/more")
    public ResponseEntity homePage2(@AuthenticationPrincipal UserLogin authUser,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        if (authUser == null)
            return ResponseEntity.ok(homeUserService.queryAll(authUser.getUid(), new PageRequest(page, size)));
        else
            return ResponseEntity.ok(homeUserService.queryAll(new PageRequest(page, size)));
    }


    @ApiOperation(value = "附近的人", notes = "（注：如果是已登陆用户，会将该用户的位置设定为传入的经纬度信息）需要传入：性别（可选，不填则表示所有），当前经纬度信息，测量距离，分页信息。分页默认：第 0 页，10 条。返回值中 distance 单位为：千米")
    @GetMapping("/homepage/nearby")
    public ResponseEntity getNearBy(@AuthenticationPrincipal UserLogin authUser,
                                    @RequestParam(required = false) UserLogin.GENDER gender,
                                    BigDecimal longitude,
                                    BigDecimal latitude,
                                    @RequestParam(defaultValue = "5") Long distance,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        Long uid = 0L;
        if (authUser != null) {
            userPositionService.updatePosition(authUser.getUid(), longitude, latitude);
            uid = authUser.getUid();
        }
        if (gender != null) {
            return ResponseEntity.ok(userPositionService.findNear(uid, gender, distance, longitude, latitude, new PageRequest(page, size)));
        }
        return ResponseEntity.ok(userPositionService.findNear(uid, distance, longitude, latitude, new PageRequest(page, size)));
    }
}
