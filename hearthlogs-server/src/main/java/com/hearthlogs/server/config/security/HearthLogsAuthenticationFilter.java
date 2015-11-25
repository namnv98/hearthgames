package com.hearthlogs.server.config.security;

import com.hearthlogs.server.database.domain.BetaSignUp;
import com.hearthlogs.server.database.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
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

public class HearthLogsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private OAuth2RestOperations restTemplate;
    private GameService gameService;

    protected HearthLogsAuthenticationFilter(String defaultFilterProcessesUrl, OAuth2RestOperations restTemplate, GameService gameService) {
        super(defaultFilterProcessesUrl);
        this.restTemplate = restTemplate;
        this.gameService = gameService;
        setAuthenticationManager(authentication -> authentication); // AbstractAuthenticationProcessingFilter requires an authentication manager.
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        OAuth2AccessToken token = restTemplate.getAccessToken();

        final ResponseEntity<UserInfo> userInfoResponseEntity = restTemplate.getForEntity("https://us.api.battle.net/account/user?access_token="+token.getValue(), UserInfo.class);

        if (userInfoResponseEntity != null && userInfoResponseEntity.getBody() != null) {
            BetaSignUp betaSignUp = gameService.getByBattletag(userInfoResponseEntity.getBody().getBattletag());
            List<GrantedAuthority> authorities = new ArrayList<>();
            if ("Seekay#1617".equals(userInfoResponseEntity.getBody().getBattletag())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else if (betaSignUp == null) {
                throw new AuthenticationServiceException("Not signed up.");
            } else if (betaSignUp.isApproved()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            return new PreAuthenticatedAuthenticationToken(userInfoResponseEntity.getBody(), empty(), authorities);
        }
        throw new AuthenticationServiceException("Not signed up.");
    }
}
