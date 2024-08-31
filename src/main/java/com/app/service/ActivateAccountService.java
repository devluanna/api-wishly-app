package com.app.service;

import com.app.domain.model.ResponseDTO.AccountRecoveryDTO;
import com.app.domain.model.ResponseDTO.ActiveAccountDTO;
import com.app.domain.model.Users;
import org.springframework.stereotype.Service;

@Service
public interface ActivateAccountService {

    Users recoveryAccount(Users user, AccountRecoveryDTO accountDTO, String email);

    ActiveAccountDTO activateAccount(Users user, ActiveAccountDTO activeAccountDTO);
}
