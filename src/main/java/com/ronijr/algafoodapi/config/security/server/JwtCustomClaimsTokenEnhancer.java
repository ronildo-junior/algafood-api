package com.ronijr.algafoodapi.config.security.server;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;

public class JwtCustomClaimsTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (authentication.getPrincipal() instanceof AuthUser) {
            putCustomInfo(accessToken, authentication);
        }
        return accessToken;
    }

    private void putCustomInfo(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        var defaultAccessToken = (DefaultOAuth2AccessToken) accessToken;
        var authUser = (AuthUser) authentication.getPrincipal();
        var customInfo = new HashMap<String, Object>();
        customInfo.put("user_id", authUser.getUserId());
        customInfo.put("full_name", authUser.getFullName());
        defaultAccessToken.setAdditionalInformation(customInfo);
    }
}