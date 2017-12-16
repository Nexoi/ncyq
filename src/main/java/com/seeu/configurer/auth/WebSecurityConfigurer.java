package com.seeu.configurer.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by neo on 25/09/2017.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // TODO 方法注解开启（关闭时方便调试）
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Value("${token.interval}")
    private Integer tokenInterval;

    @Autowired
    private YWQAuthenticationProvider ywqAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(authUserService()); // 只配置了一个数据源，建议用 provider 配置多个，如 CAS、QQ、Weibo 等
        auth.authenticationProvider(ywqAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 暂时关闭 csrf 认证
                .authorizeRequests()
//                .antMatchers("/").permitAll() // 放行
//                .anyRequest().authenticated() // 验证所有的路径（不需要）
                .anyRequest().permitAll()       // 全部放行，加了 PreAuthorize 的做验证（调试环境适用）
//                .antMatchers("/api/v1/article/**","/api/v1/pure").authenticated()  // 文章类 API 都需要验证【发布功能、评论】

                .and()
                .formLogin()
                .usernameParameter("phone") // 用 email 登录
                .loginPage("/api/v1/signin")
                //设置默认登录成功跳转页面
                .defaultSuccessUrl("/signin-success")
                .failureUrl("/signin-failure")
                .successHandler(new LoginSuccessHandle())
                .permitAll()

                .and()
                //开启cookie保存用户数据
                .rememberMe()
                //设置cookie有效期
                .tokenValiditySeconds(tokenInterval)
                //设置cookie的私钥
                .key("privateKeyxx")

                .and()
                .logout()
                //默认注销行为为logout，可以通过下面的方式来修改
                .logoutUrl("/api/v1/signout")
                //设置注销成功后跳转页面，默认是跳转到登录页面
                .logoutSuccessUrl("/signout-success")
                .permitAll()


                .and()
                .httpBasic();

//        http.csrf().disable() // oauth server 不需要 csrf 防护
//                .authorizeRequests()
//                .antMatchers("/**").permitAll() //其他页面都可以访问
//                .antMatchers("/data/**").authenticated();// 需要认证即可访问
//                .and()
//                .httpBasic().disable(); // 禁止 basic 认证

        // 302 转 401 完成 REST-ful 的权限限制时返回 302 的不合理信息
        http.exceptionHandling()
//                .accessDeniedHandler(new YWQAccessDeniedHandler());
                //Actually Spring already configures default AuthenticationEntryPoint - LoginUrlAuthenticationEntryPoint
                //This one is REST-specific addition to default one, that is based on PathRequest
                .defaultAuthenticationEntryPointFor(
                        getRestAuthenticationEntryPoint(),
                        new AntPathRequestMatcher("/api/**"));

    }

    private AuthenticationEntryPoint getRestAuthenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }
}
