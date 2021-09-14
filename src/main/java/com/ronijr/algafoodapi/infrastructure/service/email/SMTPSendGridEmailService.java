package com.ronijr.algafoodapi.infrastructure.service.email;

import com.ronijr.algafoodapi.config.email.EmailProperties;
import com.ronijr.algafoodapi.domain.service.EmailService;
import com.ronijr.algafoodapi.infrastructure.exception.EmailException;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class SMTPSendGridEmailService implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void send(Message message) {
        try {
            MimeMessage mimeMessage = getMimeMessage(message);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new EmailException("Unable to send Email.", e);
        }
    }

    private MimeMessage getMimeMessage(Message message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "ISO-8859-1");
        messageHelper.setFrom(emailProperties.getSender());
        messageHelper.setTo(message.getRecipients().toArray(new String[0]));
        messageHelper.setSubject(message.getSubject());
        if (message.isHtml()) {
            messageHelper.setText(processTemplate(message), true);
        } else {
            messageHelper.setText(message.getBody(), false);
        }
        return mimeMessage;
    }

    private String processTemplate(Message message) {
        try {
            Context thymeleafContext = new Context();
            thymeleafContext.setVariables(message.getVariables());
            return templateEngine.process(message.getBody(), thymeleafContext);
        } catch (Exception e) {
            throw new EmailException("Could not process email template", e);
        }
    }
}