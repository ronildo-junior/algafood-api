package com.ronijr.algafoodapi.infrastructure.service.email;

import com.ronijr.algafoodapi.config.email.EmailProperties;

public class SMTPEmailServiceSandbox extends SMTPEmailService {
    public SMTPEmailServiceSandbox(EmailProperties emailProperties, EmailTemplateRenderer renderer) {
        super(emailProperties, renderer);
    }

    @Override
    public void send(Message message) {
        Message messageSandbox = Message.builder().
                body(message.getBody()).
                recipient(emailProperties.getSandbox().getRecipient()).
                subject(message.getSubject()).
                isHtml(message.isHtml()).
                variables(message.getVariables()).
                build();
        super.send(messageSandbox);
    }
}