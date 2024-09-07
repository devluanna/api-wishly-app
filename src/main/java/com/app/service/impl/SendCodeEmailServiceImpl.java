package com.app.service.impl;

import com.app.domain.model.ResponseDTO.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendCodeEmailServiceImpl {

    @Autowired
    private RecoveryCodeServiceImpl tokenEmailService;

    @Transactional
    public void sendRecoveryEmail(UserDTO userDto) {
        tokenEmailService.generateToken(6);

        String subject = "";
        String emailBody = "";

        tokenEmailService.sendEmailWithToken(userDto.email(), subject, emailBody);
    }


}
