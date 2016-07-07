package com.vmware.sdugar.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sourabhdugar on 4/9/16.
 */
@Component
@Configuration
public class AuthFilter extends GenericFilterBean
        /*AbstractAuthenticationProcessingFilter*/ {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    //@Autowired
    //private AuthenticationManager manager;
    //
    //@Autowired
    //private  ApplicationContext ctx;

    public AuthFilter() {

    }

    @Bean(name="authFilterRegistrationBean")
    public FilterRegistrationBean registration(AuthFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    public Map<String, String> readRequestBodyAsJson(
            ResettableStreamHttpServletRequest request) throws IOException {
        // wrappedRequest.getInputStream().read();
        String body = IOUtils.toString(request.getReader());
        ObjectMapper ob = new ObjectMapper();
        Map<String, String> jsonMap = ob.readValue(body.getBytes(), HashMap.class);
        log.info("Body seen in filter is {}", body);
        request.resetInputStream();
        return jsonMap;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("My filter called for request {}", httpRequest.getRequestURL());
        ResettableStreamHttpServletRequest wrappedRequest =
                new ResettableStreamHttpServletRequest((HttpServletRequest) request);
        Map<String, String> body = readRequestBodyAsJson(wrappedRequest);

        final GrantedAuthority authority;

        if (body.getOrDefault("user", "unknown").equals("admin1")) {
            authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        } else {
            authority = new SimpleGrantedAuthority("ROLE_ANON");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        //Authentication auth =
        //    manager.authenticate(new RememberMeAuthenticationToken("secret", "admin1", authorities));
        //Authentication auth = new RememberMeAuthenticationToken("any", "any", authorities);
        //auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(
                new RememberMeAuthenticationToken("secret", "admin1", authorities));
        filterChain.doFilter(wrappedRequest, response);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    /*@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken("smith", "none"));
    }*/

    /**
     * Credits : https://gist.github.com/calo81/2071634
     *
     */
    private static class ResettableStreamHttpServletRequest extends
            HttpServletRequestWrapper {

        private byte[] rawData;
        private HttpServletRequest request;
        private ResettableServletInputStream servletStream;

        public ResettableStreamHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.request = request;
            this.servletStream = new ResettableServletInputStream();
        }

        public void resetInputStream() {
            servletStream.stream = new ByteArrayInputStream(rawData);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader(),
                        this.request.getCharacterEncoding());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return servletStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader(),
                        this.request.getCharacterEncoding());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return new BufferedReader(new InputStreamReader(servletStream,
                    request.getCharacterEncoding()));
        }


        private class ResettableServletInputStream extends ServletInputStream {

            private InputStream stream;

            @Override
            public int read() throws IOException {
                return stream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        }
    }
}
