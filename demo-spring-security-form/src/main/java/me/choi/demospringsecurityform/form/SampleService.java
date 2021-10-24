package me.choi.demospringsecurityform.form;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SampleService {

    public void dashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal(); // 누구
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 권한
        Object credentials = authentication.getCredentials();
        boolean authenticated = authentication.isAuthenticated();
    }

}
