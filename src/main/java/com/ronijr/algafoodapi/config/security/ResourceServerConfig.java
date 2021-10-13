package com.ronijr.algafoodapi.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and()
            //only tests, not safe to production.
            .csrf().disable()
            .cors().and()
            .oauth2ResourceServer()
                //DEFAULT_MAX_CLOCK_SKEW = 60 Seconds, then token expiration time = exp + 60s
                .jwt()
                    .jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    /*@Override
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
    }*/

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwt.getClaimAsStringList("authorities");
            if (authorities == null) {
                authorities = Collections.emptyList();
            }
            var scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> grantedAuthorities = scopesAuthoritiesConverter.convert(jwt);
            grantedAuthorities.addAll(authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
            return grantedAuthorities;
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