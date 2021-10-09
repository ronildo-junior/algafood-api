package com.ronijr.algafoodapi.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                    .cors()
                .and()
                    .oauth2ResourceServer()
                        //.opaqueToken()
                    //DEFAULT_MAX_CLOCK_SKEW = 60 Seconds, then token expiration time = exp + 60s
                    .jwt();
    }

    /* Symmetric Key
    @Bean
    public JwtDecoder jwtDecoder() {
        var secretKey = new SecretKeySpec("1234567890-!@#$%¨&*()-qwertyuiop-asdfghjklç-mnbvzxcv".getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }*/
}