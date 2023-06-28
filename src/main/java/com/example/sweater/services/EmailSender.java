package com.example.sweater.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void send(String emailTo, String subject , String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
