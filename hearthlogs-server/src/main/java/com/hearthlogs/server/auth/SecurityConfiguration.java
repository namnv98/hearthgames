package com.hearthlogs.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2ClientContextFilter oAuth2ClientContextFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String LOGIN_URL = "/login";
        http.addFilterAfter(oAuth2ClientContextFilter, AbstractPreAuthenticatedProcessingFilter.class)
            .addFilterAfter(new HearthLogsAuthenticationFilter(LOGIN_URL), OAuth2ClientContextFilter.class)
            .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL))
            .and().authorizeRequests()
            .antMatchers(GET, "/").permitAll()
            .antMatchers(POST, "/upload").permitAll()
            .antMatchers(GET, "/test").authenticated().and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .permitAll();
    }
}