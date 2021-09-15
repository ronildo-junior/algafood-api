package com.ronijr.algafoodapi.infrastructure.service.email;

import com.ronijr.algafoodapi.domain.service.EmailService;
import com.ronijr.algafoodapi.infrastructure.exception.TemplateRendererException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
@AllArgsConstructor
public class EmailTemplateRenderer {
    private final SpringTemplateEngine templateEngine;

    public String processTemplate(EmailService.Message message) {
        try {
            Context thymeleafContext = new Context();
            thymeleafContext.setVariables(message.getVariables());
            return templateEngine.process(message.getBody(), thymeleafContext);
        } catch (Exception e) {
            throw new TemplateRendererException("Could not process email template.", e);
        }
    }
}