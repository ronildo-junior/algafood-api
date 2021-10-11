package com.ronijr.algafoodapi.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST).hasAuthority("WRITE")
                .antMatchers(HttpMethod.PUT).hasAuthority("WRITE")
                .antMatchers(HttpMethod.GET).authenticated()
                .anyRequest().denyAll()
            .and()
                .cors()
            .and()
                .oauth2ResourceServer()
                        //.opaqueToken()
                    //DEFAULT_MAX_CLOCK_SKEW = 60 Seconds, then token expiration time = exp + 60s
                .jwt()
                    .jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwt.getClaimAsStringList("authorities");
            if (authorities == null) {
                authorities = Collections.emptyList();
            }
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return jwtAuthenticationConverter;
    }

    /* Symmetric Key
    @Bean
    public JwtDecoder jwtDecoder() {
        var secretKey = new SecretKeySpec("1234567890-!@#$%¨&*()-qwertyuiop-asdfghjklç-mnbvzxcv".getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }*/
}