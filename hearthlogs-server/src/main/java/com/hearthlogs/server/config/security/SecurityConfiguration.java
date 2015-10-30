package com.hearthlogs.server.config.security;

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

    @Autowired
    private HearthLogsAuthenticationFilter hearthLogsAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterAfter(oAuth2ClientContextFilter, AbstractPreAuthenticatedProcessingFilter.class);
        http.addFilterAfter(hearthLogsAuthenticationFilter, OAuth2ClientContextFilter.class);

        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));

        http.authorizeRequests().antMatchers(GET, "/").permitAll();
        http.authorizeRequests().antMatchers(POST, "/upload").permitAll();

        http.authorizeRequests().antMatchers(GET, "/match/**").authenticated();

        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").permitAll();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}