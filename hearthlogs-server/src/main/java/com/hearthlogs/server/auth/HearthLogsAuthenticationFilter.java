package com.hearthlogs.server.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.empty;
import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;

public class HearthLogsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    private OAuth2RestOperations restTemplate;

    protected HearthLogsAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(authentication -> authentication); // AbstractAuthenticationProcessingFilter requires an authentication manager.
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        OAuth2AccessToken token = restTemplate.getAccessToken();

        final ResponseEntity<UserInfo> userInfoResponseEntity = restTemplate.getForEntity("https://us.api.battle.net/account/user?access_token="+token.getValue(), UserInfo.class);
        return new PreAuthenticatedAuthenticationToken(userInfoResponseEntity.getBody(), empty(), NO_AUTHORITIES);
    }
}
