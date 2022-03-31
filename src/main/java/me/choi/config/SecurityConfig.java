package me.choi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/account/**")
                        .authorizeRequests()
                                .anyRequest().permitAll();
        http.formLogin();
        http.httpBasic();
//        http.authorizeRequests()
//                .mvcMatchers("/", "/info").permitAll()
//                .mvcMatchers("/admin").hasRole("ADMIN")
//                .mvcMatchers("/account/**").permitAll()
//                .anyRequest().authenticated();
//        http.formLogin();
//        http.httpBasic();
    }

}
