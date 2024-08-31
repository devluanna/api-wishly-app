package com.app.service;

import com.app.domain.model.ResponseDTO.PasswordDTO;
import com.app.domain.model.ResponseDTO.AccountRecoveryDTO;
import com.app.domain.model.Users;
import org.springframework.stereotype.Service;

@Service
public interface PasswordService {

    Users toUpdatePassword (Users user, PasswordDTO passwordDTO, Integer idUser);

    Users recoveryPassword(Users user, AccountRecoveryDTO recoveryDTO, String email);
}
