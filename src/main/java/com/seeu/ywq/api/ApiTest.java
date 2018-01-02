//package com.seeu.ywq.api.release;
//
//import com.seeu.ywq.page.model.HomePageUser;
//import com.seeu.ywq.page.service.AppHomePageService;
//import com.seeu.ywq.user.service.FansService;
//import com.seeu.ywq.user.service.UserPositionService;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/test")
//public class ApiTest {
//    @Resource
//    private FansService fansService;
//
//    @Resource
//    private AppHomePageService appPageService;
//
//    @Resource
//    private UserPositionService userPositionService;
//
//    @GetMapping
//    public ResponseEntity get(@RequestParam Long x, Long y, BigDecimal xx, BigDecimal yy) {
//        Page page = userPositionService.findNear(2l, xx, yy, new PageRequest(0, 10));
//        return ResponseEntity.ok(page);
//    }
//}
