package com.app.service.impl;

import com.app.utils.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class RecoveryCodeServiceImpl {

    @Autowired
    private MailConfig emailService;

    public String generateToken(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            token.append(characters.charAt(index));
        }

        return token.toString();
    }

    public void sendEmailWithToken(String emailAddress, String subject, String body) {
        emailService.sendEmail(emailAddress, subject, body);
    }

}