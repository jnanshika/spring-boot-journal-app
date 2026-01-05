package com.pm.journalapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail(){
        emailService.sendEmail(
                "anshikabca3rdyearz1@gmail.com",
                "Testing java mail sender",
                "Hey, there!"
        );
    }
}
