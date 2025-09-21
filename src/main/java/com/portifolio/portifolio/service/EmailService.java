package com.portifolio.portifolio.service;

import com.portifolio.portifolio.model.ContactRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
@Service
public class EmailService
{
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.to}")
    private String to;

    public void sendContactEmail(ContactRequest contactRequest) throws MessagingException
    {
        sendNotificationEmail(contactRequest);
        sendConfirmationEmaill(contactRequest);
    }

    private void sendNotificationEmail(ContactRequest contactRequest) throws MessagingException
    {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        Context context = new Context();
        context.setVariable("name", contactRequest.getName());
        context.setVariable("email", contactRequest.getEmail());
        context.setVariable("subject", contactRequest.getSubject());
        context.setVariable("message", contactRequest.getSubject());

        String htmlContent = templateEngine.process("contact-notif", context);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setSubject("New Contact Form Submission: "+contactRequest);
        mimeMessageHelper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
        logger.info("Notification email sent successfully to {}", to);
    }

    private void sendConfirmationEmaill(ContactRequest contactRequest) throws  MessagingException
    {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        Context context = new Context();
        context.setVariable("name", contactRequest.getName());
        String htmlContent = templateEngine.process("contact-confirm", context);
        mimeMessageHelper.setTo(contactRequest.getEmail());
        mimeMessageHelper.setSubject("Thank you for reaching out!");
        mimeMessageHelper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
        logger.info("Confirmation email sent successfully to {}", contactRequest.getEmail());
    }

}
