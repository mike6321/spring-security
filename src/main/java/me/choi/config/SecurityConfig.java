package me.choi.config;

import me.choi.account.AccountService;
import me.choi.account.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;

    public SecurityExpressionHandler expressionHandler() {
        /**
         * Hierarchy 설정
         * */
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        /**
         * Handler 설정
         * */
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        return handler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/signup").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .mvcMatchers("/user").hasRole("USER")
                .mvcMatchers("/account/**").permitAll()
                .anyRequest().authenticated()
                .expressionHandler(expressionHandler());

        http.formLogin().loginPage("/login").permitAll();
        http.formLogin()
            .loginPage("/login")
            .permitAll();

        http.rememberMe()
            .userDetailsService(accountService)
            .key("remember-me-sample");

        http.httpBasic();

        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");

        // TODO: 2022/04/06 ExceptionTranslationFilter -> FilterSecurityInterceptor
        /**
         * FilterSecurityInterceptor (AccessDecisionManager, AffirmativeBased)
         * 발생할 수 있는 예외
         *  AuthenticationException -> AuthenticationEntryPoint: 해당 유저가 인증할 수 있도록 인증이 가능한 페이지로 이동 (ex. 로그인하지 않고 대시보드 접근)
         *  AccessDeniedException -> AccessDeniedHandler: 유저로 로그인 했을 때 어드민 페이지 접근
         * */
        http.exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler());

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().mvcMatchers("/favicon.ico");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        /**
         * ignoring을 하면 filter의 목록이 비어지게 된다.
         * */
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
