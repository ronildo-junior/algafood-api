package com.ronijr.algafoodapi.config.email;

import com.ronijr.algafoodapi.domain.service.EmailService;
import com.ronijr.algafoodapi.infrastructure.service.email.EmailTemplateRenderer;
import com.ronijr.algafoodapi.infrastructure.service.email.FakeEmailService;
import com.ronijr.algafoodapi.infrastructure.service.email.SMTPEmailService;
import com.ronijr.algafoodapi.infrastructure.service.email.SMTPEmailServiceSandbox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {
    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final EmailTemplateRenderer templateEngine;

    @Bean
    public EmailService emailService() {
        switch (emailProperties.getImpl()) {
            case SMTP:
                return new SMTPEmailService(mailSender, emailProperties, templateEngine);
            case SMTP_SANDBOX:
                return new SMTPEmailServiceSandbox(mailSender, emailProperties, templateEngine);
            default:
                return new FakeEmailService(templateEngine);
        }
    }
}