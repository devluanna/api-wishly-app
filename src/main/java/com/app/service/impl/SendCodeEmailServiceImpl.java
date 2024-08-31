package com.app.service.impl;

import com.app.domain.model.ResponseDTO.UserDTO;
import com.app.domain.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendCodeEmailServiceImpl {

    @Autowired
    private ActivationRecoveryCodeServiceImpl tokenEmailService;

    @Transactional
    public void sendRecoveryEmail(UserDTO userDto) {
        tokenEmailService.generateToken(6);

        String subject = "";
        String emailBody = "";

        tokenEmailService.sendEmailWithToken(userDto.email(), subject, emailBody);
    }


}
