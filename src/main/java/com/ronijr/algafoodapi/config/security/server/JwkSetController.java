package com.ronijr.algafoodapi.config.security.server;

import com.nimbusds.jose.jwk.JWKSet;
import com.ronijr.algafoodapi.infrastructure.EnvironmentConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Profile(value = { EnvironmentConstants.HOMOLOGATION, EnvironmentConstants.PRODUCTION })
@RestController
public class JwkSetController {
    @Autowired
    private JWKSet jwkSet;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() {
        return this.jwkSet.toJSONObject();
    }
}