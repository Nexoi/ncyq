package com.seeu.ywq.api.userlogin;

import com.seeu.core.R;
import com.seeu.ywq.user.service.UserPositionService;
import com.seeu.ywq.userlogin.exception.JwtCodeException;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.repository.TokenPersistentRepository;
import com.seeu.ywq.utils.AppAuthFlushService;
import com.seeu.ywq.utils.jwt.JwtConstant;
import com.seeu.ywq.utils.jwt.JwtUtil;
import com.seeu.ywq.utils.jwt.PhoneCodeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 该 api 登陆/注销功能已由 spring security 自动实现
 */
@Api(tags = "用户登录", description = "登录/注销", position = 0)
@RestController
public class SignInOutApi {

    @Autowired
    private UserPositionService userPositionService;
    @Resource
    private TokenPersistentRepository tokenPersistentRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtConstant jwtConstant;
    @Autowired
    private AppAuthFlushService appAuthFlushService;


    @ApiOperation("刷新 TOKEN")
    @GetMapping("/api/v1/token")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity flushToken(@AuthenticationPrincipal UserLogin authUser) {
        PhoneCodeToken codeToken = new PhoneCodeToken();
        codeToken.setPhone(authUser.getPhone());
        codeToken.setCode("" + authUser.getUid());
        String subject = jwtUtil.generalSubject(codeToken);
        try {
            String token = jwtUtil.createJWT(jwtConstant.getJWT_ID(), subject, jwtConstant.getJWT_TOKEN_INTERVAL());
            Map map = new HashMap();
            map.put("token", token);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(R.code(500).message("TOKEN 生成失败！请稍后再试"));
        }
    }

    @ApiOperation("用 TOKEN 登录")
    @PostMapping("/api/v1/signin/use-token")
    public ResponseEntity resetUserContext(@RequestParam(required = true) String token) {
        // 验证 TOKEN
        if (token == null || token.trim().length() == 0)
            return ResponseEntity.status(403).body(R.code(403).message("TOKEN 验证失败！"));
        // jwt 解析
        PhoneCodeToken phoneCodeToken = jwtUtil.parseToken(token);
        if (phoneCodeToken == null
                || phoneCodeToken.getPhone() == null
                || phoneCodeToken.getCode() == null
                ) {
            return ResponseEntity.status(403).body(R.code(403).message("TOKEN 验证失败！"));
        }
        appAuthFlushService.flush(Long.parseLong(phoneCodeToken.getCode()));
        return ResponseEntity.ok().build();
    }


    @ApiResponse(code = 200, message = "登陆成功")
    @ApiOperation(value = "登录账号", notes = "登陆成功只会返回 200 状态码，token 信息会自动写入 cookie，客户端需要支持 cookie；如需要退出账号，请使用 /api/v1/signout 清除 cookie 信息")
    @PostMapping("/api/v1/signin")
    public void signIn(@AuthenticationPrincipal UserLogin authUser,
                       @ApiParam(value = "登录帐号（手机/openId/...）")
                       @RequestParam String username,
                       @RequestParam String password,
                       @RequestParam(value = "remember-me", required = false) Boolean rememberMe,
                       @RequestParam(required = false) BigDecimal longitude,
                       @RequestParam(required = false) BigDecimal latitude) {
        // 更新登陆信息
        if (latitude != null && longitude != null && authUser != null) {
            userPositionService.updatePosition(authUser.getUid(), longitude, latitude);
        }
        return;
    }

    @ApiResponse(code = 200, message = "注销成功")
    @ApiOperation(value = "注销用户", notes = "退出登陆时调用此接口，可以清除服务器缓存信息，用户需再次登录获取新的 token 才能继续访问")
    @PostMapping("/api/v1/signout")
    public ResponseEntity signOut(@AuthenticationPrincipal UserLogin authUser) {
        if (authUser != null)
            tokenPersistentRepository.deleteByUsername(authUser.getUsername());
        return ResponseEntity.ok(R.code(200).message("注销成功").build());
    }


    @GetMapping("/signin-success")
    @ApiResponse(code = 200, message = "登陆成功")
    public ResponseEntity signInSuccess() {
        return ResponseEntity.ok().body(R.code(200).message("登陆成功").build());
    }

    @GetMapping("/signin-failure")
    @ApiResponse(code = 400, message = "登陆失败，账号/密码错误")
    public ResponseEntity signInFailure() {
        return ResponseEntity.badRequest().body(R.code(400).message("登陆失败，账号/密码错误").build());
    }

    @GetMapping("/signout-success")
    @ApiResponse(code = 200, message = "注销成功")
    public ResponseEntity signOutSuccess() {
        return ResponseEntity.ok(R.code(200).message("注销成功").build());
    }
}
