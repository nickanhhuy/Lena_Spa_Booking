package com.example.backend_lena.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@   Service
public class EmailTestService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestEmail(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);              // recipient
            message.setSubject("Test Email");    // subject
            message.setText("This is a test email from backend_Lena."); // body
            message.setFrom("huynguyen.study3054@gmail.com"); // your Gmail address

            mailSender.send(message);
            System.out.println("Test email sent successfully to " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

