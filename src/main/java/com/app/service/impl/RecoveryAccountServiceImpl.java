package com.app.service.impl;

import com.app.domain.model.ResponseDTO.AccountRecoveryDTO;
import com.app.domain.model.ResponseDTO.ActiveAccountDTO;
import com.app.domain.model.Status;
import com.app.domain.model.Users;
import com.app.domain.repository.UserRepository;
import com.app.exception.BusinessRuleException;
import com.app.service.ActivateAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RecoveryAccountServiceImpl implements ActivateAccountService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecoveryCodeServiceImpl codeService;

    @Autowired
    PasswordServiceImpl passwordService;

    @Override
    public ActiveAccountDTO activateAccount(Users user, ActiveAccountDTO activeAccountDTO) {

        Users existingUser = userRepository.findByEmail(activeAccountDTO.email());

        if (existingUser == null) {
            throw new BusinessRuleException("Invalid email", HttpStatus.BAD_REQUEST);
        }

        if (codeService.isTokenExpired(existingUser.getTokenExpiration())) {
            throw new BusinessRuleException("The activation token has expired.", HttpStatus.BAD_REQUEST);
        }

        validateActivationCode(existingUser, user);

        validationPasswordAndCode(existingUser, user);

        Users activatedUser = userRepository.save(existingUser);

        return responseUserActivateDTO(activatedUser);

    }

    private void validateActivationCode(Users existingUser, Users user) {
        String activationConfirmCode = user.getConfirm_code_activation();
        String storedActivationCode = existingUser.getCode_account_activation();

        if (activationConfirmCode == null || !activationConfirmCode.equals(storedActivationCode)) {
            throw new BusinessRuleException("Invalid activation code, unable to activate account.", HttpStatus.BAD_REQUEST);
        }
    }


    public void validationPasswordAndCode(Users existingUser, Users user) {
        passwordService.validationsPassword(user.getPassword(), user.getConfirm_password());
        String encryptedNewPassword = passwordService.encryptPassword(user.getPassword());
        String encryptedConfirmNewPassword = passwordService.encryptPassword(user.getConfirm_password());

        existingUser.setPassword(encryptedNewPassword);
        existingUser.setConfirm_password(encryptedConfirmNewPassword);
        existingUser.setConfirm_code_activation(user.getConfirm_code_activation());
        existingUser.setTokenValidate(true); //temporário até que o tempo de expiração do token seja implementado
        existingUser.setCode_account_activation(null);

        existingUser.setStatus(Status.ACTIVATED);
    }

    public ActiveAccountDTO responseUserActivateDTO(Users userActived) {

        return new ActiveAccountDTO(
                userActived.getId_user(),
                userActived.getEmail(),
                userActived.getConfirm_code_activation(),
                userActived.getStatus()
        );

    }

    @Override
    public Users recoveryAccount(Users user, AccountRecoveryDTO accountDTO, String email) {

        Users existingUser = userRepository.findByEmail(accountDTO.email());

       if (existingUser != null && existingUser.getStatus().equals(Status.DISABLED)) {

           String tokenRecoveryUser = codeService.generateToken(6);
           Date tokenExpiration = codeService.generateTokenExpiration();

           existingUser.setCode_account_activation(tokenRecoveryUser);
           existingUser.setTokenExpiration(tokenExpiration);

            System.out.println("TOKEN PRA ATIVAR A CONTA:" + tokenRecoveryUser);

            userRepository.save(existingUser);

            //sendEmailRecovery(existingUser);

            return existingUser;
        } else {
            throw new BusinessRuleException("Email does not match any user's email.", HttpStatus.BAD_REQUEST);
        }
    }


    public void sendEmailRecovery(Users existingUser) {
        String tokenRecoveryUser = codeService.generateToken(6);

        existingUser.setCode_account_activation(tokenRecoveryUser);

        String activationLink = "https://wishly.com/activate-account?email=" + existingUser.getEmail();

        userRepository.save(existingUser);

        String subject = "Wishly App - Activating your account! ";
        String emailBody = "Phew!!! It's good to see you back here :')" + " " + existingUser.getFirst_name() + " " + existingUser.getLast_name() +
                ",\n\nWelcome back!\n\n" +
                "Here is your account recovery code, once you click on the link update your password.\n\n" +
                "Login identity: " + existingUser.getUsername() + "\n" +
                "Token for Recovery: " + tokenRecoveryUser + "\n" + "\n" +
                "Click the link below to recover your account and set a new password:\n\n" +
                activationLink + "\n\n" +
                "Remember to reset and make your password strong for security.\n\n";


        codeService.sendEmailWithToken(existingUser.getEmail(), subject, emailBody);
    }



}
