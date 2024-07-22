package com.example.uslugicykliczne.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService{

    private final JavaMailSender emailSender;
    private final String recipientEmail;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
        recipientEmail = "bartlomiejskwara731@gmail.com";
    }

    public void sendEmailNotification(String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cyclicservicesreminder@gmail.com");
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }
}