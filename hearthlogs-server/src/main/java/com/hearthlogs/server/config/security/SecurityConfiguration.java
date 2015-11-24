package com.hearthlogs.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2ClientContextFilter oAuth2ClientContextFilter;

    @Autowired
    private OAuth2RestOperations restTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String LOGIN_URL = "/login";
        http.addFilterAfter(oAuth2ClientContextFilter, AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(new HearthLogsAuthenticationFilter(LOGIN_URL, restTemplate), OAuth2ClientContextFilter.class)
                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL))
                .and().authorizeRequests()
                .antMatchers(GET, "/").permitAll()
                .antMatchers(GET, "/where").permitAll()
                .antMatchers(POST, "/upload").authenticated()
                .antMatchers(GET, "/test").authenticated().and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();

    }
}