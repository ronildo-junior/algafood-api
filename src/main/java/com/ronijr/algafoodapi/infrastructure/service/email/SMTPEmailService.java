package com.ronijr.algafoodapi.infrastructure.service.email;

import com.ronijr.algafoodapi.config.email.EmailProperties;
import com.ronijr.algafoodapi.domain.service.EmailService;
import com.ronijr.algafoodapi.infrastructure.exception.EmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
public class SMTPEmailService implements EmailService {
    protected final EmailProperties emailProperties;
    protected final EmailTemplateRenderer renderer;
    @Autowired protected JavaMailSender mailSender;

    @Override
    public void send(Message message) {
        try {
            MimeMessage mimeMessage = this.getMimeMessage(message);
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
            messageHelper.setText(renderer.processTemplate(message), true);
        } else {
            messageHelper.setText(message.getBody(), false);
        }
        return mimeMessage;
    }
}