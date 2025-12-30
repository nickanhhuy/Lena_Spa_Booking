package com.example.backend_lena.controller;

import com.example.backend_lena.service.ResendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-email")
public class TestEmailController {

    @Autowired
    private ResendEmailService resendEmailService;

    @GetMapping("/get")
    public String sendTest() {
        resendEmailService.sendTestEmail("huynguyen.study3054@gmail.com");
        return "Email sent via Resend! Check your inbox.";
    }
}
