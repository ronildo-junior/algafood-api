package com.ronijr.algafoodapi.config.email;

import com.ronijr.algafoodapi.domain.service.EmailService;
import com.ronijr.algafoodapi.infrastructure.service.email.EmailTemplateRenderer;
import com.ronijr.algafoodapi.infrastructure.service.email.FakeEmailService;
import com.ronijr.algafoodapi.infrastructure.service.email.SMTPEmailService;
import com.ronijr.algafoodapi.infrastructure.service.email.SMTPEmailServiceSandbox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {
    private final EmailProperties emailProperties;
    private final EmailTemplateRenderer templateEngine;

    @Bean
    public EmailService emailService() {
        switch (emailProperties.getImpl()) {
            case SMTP:
                return new SMTPEmailService(emailProperties, templateEngine);
            case SMTP_SANDBOX:
                return new SMTPEmailServiceSandbox(emailProperties, templateEngine);
            default:
                return new FakeEmailService(templateEngine);
        }
    }
}