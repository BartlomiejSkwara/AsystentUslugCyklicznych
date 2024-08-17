package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService{

    private final JavaMailSender emailSender;
    private final String recipientEmail;
    private final DateTimeFormatter formatter;
    private final String sender;

    private final TemplateEngine templateEngine;
    public EmailService(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        //recipientEmail = "gdd58951@doolk.com";
        recipientEmail = "bartlomiejskwara731@gmail.com";
        sender = "cyclicservicesreminder@gmail.com";
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }


    public void sendEmailNotification(String subject, CyclicalServiceEntity cyclicalServiceEntity) {


        MimeMessage  mimeMessage  = this.emailSender.createMimeMessage();
        try {
            MimeMessageHelper message =  new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject(subject);
            message.setFrom(sender);
            message.setTo(recipientEmail);


            final Context ctx = new Context(); //locale
            ctx.setVariable("service", cyclicalServiceEntity);
            ctx.setVariable("dysponent", cyclicalServiceEntity.getDysponentEntity());
            ctx.setVariable("customer", cyclicalServiceEntity.getCustomerEntity());
            //ctx.setVariable("imageResourceName", imageResourceName);

            final String htmlContent = this.templateEngine.process("emailTemplate.html", ctx);
            message.setText(htmlContent, true);
            emailSender.send(message.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }




        //SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom();
//        message.setTo(recipientEmail);
//        message.setSubject(subject);
//        message.setText(text);


    }
}