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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ActivateAccountServiceImpl implements ActivateAccountService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivationRecoveryCodeServiceImpl codeService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PasswordServiceImpl passwordService;

    @Override
    public ActiveAccountDTO activateAccount(Users user, ActiveAccountDTO activeAccountDTO) {

        Users existingUser = userRepository.findByEmail(activeAccountDTO.email());

        if (existingUser == null) {
            throw new BusinessRuleException("Email invalid", HttpStatus.BAD_REQUEST);
        }

        existingUser.setConfirm_code_activation(activeAccountDTO.confirm_code_activation());

        userRepository.save(existingUser);

        if (user.getConfirm_code_activation() != null && user.getConfirm_code_activation().equals(existingUser.getCode_account_activation())) {

            passwordService.validationsPassword(user.getPassword(), user.getConfirm_password());
            String encryptedNewPassword = passwordService.encryptPassword(user.getPassword());
            String encryptedConfirmNewPassword = passwordService.encryptPassword(user.getConfirm_password());

            existingUser.setPassword(encryptedNewPassword);
            existingUser.setConfirm_password(encryptedConfirmNewPassword);
            existingUser.setConfirm_code_activation(user.getConfirm_code_activation());


            existingUser.setStatus(Status.DISABLED);

            Users userActived = userRepository.save(existingUser);
            return responseUserActivateDTO(userActived);

        } else {
            throw new BusinessRuleException("Invalid code, unable to activate account, please try again later.", HttpStatus.BAD_REQUEST);
        }
    }

    public ActiveAccountDTO responseUserActivateDTO(Users userActived) {
        ActiveAccountDTO responseDTO = new ActiveAccountDTO(
                userActived.getId_user(),
                userActived.getEmail(),
                userActived.getCode_account_activation(),
                userActived.getCode_account_activation(),
                userActived.getPassword(),
                userActived.getConfirm_password(),
                userActived.getStatus()
        );

        return responseDTO;

    }

    @Override
    public Users recoveryAccount(Users user, AccountRecoveryDTO accountDTO, String email) {

        Users existingUser = userRepository.findByEmail(accountDTO.email());

        if (existingUser != null) {

            String tokenRecoveryUser = codeService.generateToken(6);
            //String encryptedPassword = passwordEncoder.encode(tokenRecoveryUser);

            existingUser.setCode_account_activation(tokenRecoveryUser);

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
        String encryptedPassword = passwordEncoder.encode(tokenRecoveryUser);

        existingUser.setCode_account_activation(encryptedPassword);

        String activationLink = "https://wishly.com/activate-account?email=" + existingUser.getEmail();

        userRepository.save(existingUser);

        String subject = "Wishly App - Activating your account! ";
        String emailBody = "Phew!!! It's good to see you back here :')" + " " + existingUser.getFirst_name() + " " + existingUser.getLast_name() +
                ",\n\nWelcome back!\n\n" +
                "Here is your account activation code, once you click on the link update your password.\n\n" +
                "Login identity: " + existingUser.getUsername() + "\n" +
                "Token for Recovery: " + tokenRecoveryUser + "\n" + "\n" +
                "Please click the link below to activate your account and set a new password:\n\n" +
                activationLink + "\n\n" +
                "Remember to reset and make your password strong for security.\n\n";


        codeService.sendEmailWithToken(existingUser.getEmail(), subject, emailBody);
    }




}
