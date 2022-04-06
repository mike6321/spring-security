package me.choi.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .expressionHandler(expressionHandler())
        ;
        http.formLogin().loginPage("/login").permitAll();
        http.httpBasic();

        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");

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
}
