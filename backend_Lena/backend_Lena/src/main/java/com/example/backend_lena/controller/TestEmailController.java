package com.example.backend_lena.controller;

import com.example.backend_lena.service.EmailTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-email")
public class TestEmailController {

    @Autowired
    private EmailTestService emailTestService;

    @GetMapping
    public String sendTest() {
        emailTestService.sendTestEmail("huynguyen.study3054@gmail.com"); // replace with any email you can check
        return "Test email request sent!";
    }
}
