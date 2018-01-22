package com.seeu.configurer.auth;

import com.seeu.ywq.userlogin.model.ThirdUserLogin;
import com.seeu.ywq.userlogin.model.USER_STATUS;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.ThirdPartTokenService;
import com.seeu.ywq.userlogin.service.ThirdUserLoginService;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by neo on 08/10/2017.
 */
@Component("seeuAuthenticationProvider")
public class YWQAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserReactService userReactService;
    @Autowired
    private ThirdUserLoginService thirdUserLoginService;
    @Autowired
    private ThirdPartTokenService thirdPartTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 普通 账号／密码 验证
        // 用户必须为【正常状态】用户
        UserLogin user = userReactService.findByPhone(name);
        if (user != null
                && user.getPassword().equals(password)
                && user.getMemberStatus() != null
                && user.getMemberStatus() != USER_STATUS.UNACTIVED
                && user.getMemberStatus() != USER_STATUS.DISTORY) {
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        }


        // third part login
        // password 就是 token
        ThirdUserLogin tul = thirdUserLoginService.findByName(name);
        if (tul != null
                && tul.getYwqUid() != null) {
            // 验证第三方
            final Map<String, String> map = new HashMap();
            thirdPartTokenService.validatedInfo(tul.getType(), name, password, (isValidated, username, nickname, headIconUrl) -> {
                if (isValidated) {
                    map.put("ok", "ok");
                    map.put("nickname", nickname);
                    map.put("headIconUrl", headIconUrl);
                } else {
                    map.put("ok", "notok");
                    map.put("nickname", null);
                    map.put("headIconUrl", null);
                }
            });
            // start find uid
            if ("ok".equals(map.get("ok"))) {
                UserLogin ul = userReactService.findOne(tul.getYwqUid());
                if (ul != null
                        && ul.getMemberStatus() != null
                        && ul.getMemberStatus() != USER_STATUS.UNACTIVED
                        && ul.getMemberStatus() != USER_STATUS.DISTORY) {
                    return new UsernamePasswordAuthenticationToken(ul, password, ul.getAuthorities());
                }
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
