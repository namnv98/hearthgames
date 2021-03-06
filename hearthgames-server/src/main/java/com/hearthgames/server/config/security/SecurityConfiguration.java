package com.hearthgames.server.config.security;

import com.hearthgames.server.database.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConfigurationProperties("hearthgames")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2ClientContextFilter oAuth2ClientContextFilter;

    @Autowired
    private OAuth2RestOperations restTemplate;

    private String redirectUrl;

    @Autowired
    private GameService gameService;

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler(redirectUrl);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String loginUrl = "/login";

        HearthGamesAuthenticationFilter hearthGamesAuthenticationFilter = new HearthGamesAuthenticationFilter(loginUrl, restTemplate, gameService);
        hearthGamesAuthenticationFilter.setAuthenticationSuccessHandler(successHandler());

        http.addFilterAfter(oAuth2ClientContextFilter, AbstractPreAuthenticatedProcessingFilter.class)
            .addFilterAfter(hearthGamesAuthenticationFilter, OAuth2ClientContextFilter.class)
            .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(loginUrl))
            .and().authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/**").permitAll().and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .permitAll();
        http.csrf().disable();
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}