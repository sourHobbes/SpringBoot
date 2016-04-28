package com.vmware.sdugar.security;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by sourabhdugar on 4/9/16.
 */
public class AuthFilter extends GenericFilterBean /*AbstractAuthenticationProcessingFilter*/ {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private final AuthenticationManager manager;

    public AuthFilter(AuthenticationManager manager) {
        //super(defaultRequestUrl);
        this.manager = manager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("My filter called for request {}", httpRequest.getRequestURL());
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ANON");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        Authentication auth =
            manager.authenticate(new RememberMeAuthenticationToken("secret", "admin1", authorities));
        //Authentication auth = new RememberMeAuthenticationToken("any", "any", authorities);
        //auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    /*@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken("smith", "none"));
    }*/
}
