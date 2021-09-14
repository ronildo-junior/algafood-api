package com.ronijr.algafoodapi.config.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated @Getter @Setter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {
    @NotBlank
    private String sender;
}