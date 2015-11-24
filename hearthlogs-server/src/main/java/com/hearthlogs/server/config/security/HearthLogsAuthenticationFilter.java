package com.hearthlogs.server.config.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.empty;
import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;

public class HearthLogsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private OAuth2RestOperations restTemplate;

    protected HearthLogsAuthenticationFilter(String defaultFilterProcessesUrl, OAuth2RestOperations restTemplate) {
        super(defaultFilterProcessesUrl);
        this.restTemplate = restTemplate;
        setAuthenticationManager(authentication -> authentication); // AbstractAuthenticationProcessingFilter requires an authentication manager.
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        OAuth2AccessToken token = restTemplate.getAccessToken();

        final ResponseEntity<UserInfo> userInfoResponseEntity = restTemplate.getForEntity("https://us.api.battle.net/account/user?access_token="+token.getValue(), UserInfo.class);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if ("Seekay#1617".equals(userInfoResponseEntity.getBody().getBattletag())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return new PreAuthenticatedAuthenticationToken(userInfoResponseEntity.getBody(), empty(), authorities);
    }
}
