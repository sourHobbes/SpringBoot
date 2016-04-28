package com.vmware.sdugar.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import javax.servlet.FilterRegistration;

/**
 * Created by sourabhdugar on 4/9/16.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public WebSecurityConfig() {
        super(true);
    }

    @Autowired
    private AuthFilter authFilter;

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http.csrf().disable()
            .exceptionHandling().and()
            .anonymous().and()
            .servletApi().and()
            .headers().and().authorizeRequests()

            //allow anonymous resource requests
            .antMatchers("/").permitAll()
            .antMatchers("/favicon.ico").permitAll()
            .antMatchers("/resources/**").permitAll()

            //allow anonymous POSTs to login
            .antMatchers(HttpMethod.POST, "/api/login").permitAll()

            //allow anonymous GETs to API
            .antMatchers(HttpMethod.GET, "/api/**").permitAll()

            //defined Admin only API area
            .antMatchers("/admin/**").hasRole("ADMIN")

            //all other request need to be authenticated
            .anyRequest().authenticated().and().antMatcher("/api/admin")

            // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new RememberMeAuthenticationProvider("secret"));
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
