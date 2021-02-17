package com.xuelei.tools.security.config;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity //启用security安全框架
@EnableGlobalMethodSecurity(prePostEnabled = true) //启用@PreAuthorize注解配置，如果不加则注解不会生效
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String encode = new BCryptPasswordEncoder().encode("123");
        log.info("passowrd:{}",encode);
        auth.inMemoryAuthentication()
                .withUser("xuelei").password(passwordEncoder().encode("123")).roles("super").and()
                .withUser("xuelei0").password(passwordEncoder().encode("123")).roles("normal");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(new SimpleUrlAuthenticationSuccessHandler(){
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        log.info("auth:{}", JSONUtil.parse(authentication));
                        response.setStatus(200);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().append("{\"code\":0,\"msg\":\"登录成功!\",\"data\":\"success\"}");
                    }
                })
                .failureHandler(new SimpleUrlAuthenticationFailureHandler(){
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        log.info("auth:{}", JSONUtil.parse(exception));
                        response.setStatus(200);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().append("{\"code\":0,\"msg\":\"登录失败("+exception.getMessage()+")!\",\"data\":\"failure\"}");
                    }
                }).and()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authException) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        resp.getWriter().append("{\"code\":0,\"msg\":\"没有权限("+authException.getMessage()+")!\",\"data\":\"failure\"}");
                    }})
                .accessDeniedHandler(new AccessDeniedHandler(){
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse resp, AccessDeniedException e) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        resp.getWriter().append("{\"code\":0,\"msg\":\"没有权限("+e.getMessage()+")!\",\"data\":\"failure\"}");
                    }
                }).and()
                .logout()
                .logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler(){

                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        log.info("auth:{}", JSONUtil.parse(authentication));
                        response.setStatus(200);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().append("{\"code\":0,\"msg\":\"登出成功("+JSONUtil.parse(authentication)+")!\",\"data\":\"failure\"}");
                    }
                })
                .deleteCookies("sessionId").and()
                .authorizeRequests()
                .antMatchers("/login","/logout").permitAll()
                .anyRequest().authenticated();

    }


}
