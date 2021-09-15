package com.ronijr.algafoodapi.infrastructure.service.email;

import com.ronijr.algafoodapi.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class FakeEmailService implements EmailService {
    private final EmailTemplateRenderer renderer;

    @Override
    public void send(Message message) {
        log.info("[FAKE] Email successfully sent to {}\n{} ", message.getRecipients().stream().
                map(Object::toString).collect(Collectors.joining(",")),
                renderer.processTemplate(message));
    }
}