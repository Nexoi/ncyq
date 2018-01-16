package com.seeu.configurer.auth;

import com.seeu.ywq.userlogin.model.ThirdUserLogin;
import com.seeu.ywq.userlogin.model.USER_STATUS;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.userlogin.service.ThirdUserLoginService;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


/**
 * Created by neo on 08/10/2017.
 */
@Component("seeuAuthenticationProvider")
public class YWQAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserReactService userReactService;
    @Autowired
    private ThirdUserLoginService thirdUserLoginService;

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
        ThirdUserLogin tul = thirdUserLoginService.findByName(name);
        if (tul != null
                && tul.getYwqUid() != null
                && tul.getCredential().equals(password)) {
            // start find uid
            UserLogin ul = userReactService.findOne(tul.getYwqUid());
            if (ul != null
                    && ul.getPassword().equals(password)
                    && ul.getMemberStatus() != null
                    && ul.getMemberStatus() != USER_STATUS.UNACTIVED
                    && ul.getMemberStatus() != USER_STATUS.DISTORY) {
                return new UsernamePasswordAuthenticationToken(ul, password, ul.getAuthorities());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
