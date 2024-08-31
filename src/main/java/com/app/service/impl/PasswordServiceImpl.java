package com.app.service.impl;

import com.app.domain.model.ResponseDTO.PasswordDTO;
import com.app.domain.model.ResponseDTO.AccountRecoveryDTO;
import com.app.domain.model.Users;
import com.app.domain.repository.UserRepository;
import com.app.exception.BusinessRuleException;
import com.app.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    UserRepository usersRepository;
    @Autowired
    ActivationRecoveryCodeServiceImpl codeService;
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users recoveryPassword(Users user, AccountRecoveryDTO recoveryDTO, String email) {
        Users existingUser = usersRepository.findByEmail(recoveryDTO.email());

        if (existingUser != null) {

           // sendEmailRecovery(existingUser);

            return existingUser;
        } else {
            throw new BusinessRuleException("Email does not match any user's email.", HttpStatus.BAD_REQUEST);
        }
    }

    public void sendEmailRecovery(Users existingUser) {
        String tokenRecoveryUser = codeService.generateToken(6);
        String encryptedPassword = passwordEncoder.encode(tokenRecoveryUser);
        existingUser.setCode_recovery_password(encryptedPassword);

        usersRepository.save(existingUser);

        String subject = "Wishly App - Password Recovery.";
        String emailBody = "Phew!!! You're back!" + " " + existingUser.getFirst_name() + " " + existingUser.getLast_name() +
                ",\n\nWelcome back!\n\n" +
                "Remember to reset and make your password strong for security.\n\n" +
                "Here is your information for recovering and updating your new password.\n\n" +
                "Login identity: " + existingUser.getUsername() + "\n" +
                "Token for Recovery: " + tokenRecoveryUser;

        codeService.sendEmailWithToken(existingUser.getEmail(), subject, emailBody);
    }

    @Override
    public Users toUpdatePassword(Users user, PasswordDTO passwordDTO, Integer id_user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessRuleException("Authentication is required to update the password.",HttpStatus.BAD_REQUEST);
        }

        Users authenticatedUser = (Users) authentication.getPrincipal();

        if (!authenticatedUser.getId_user().equals(id_user)) {
            throw new BusinessRuleException("Access denied!", HttpStatus.BAD_REQUEST);
        }

        passwordValidateAndUpdate(user);

        return usersRepository.save(user);
    }

    public void passwordValidateAndUpdate(Users updatedNewPassword) {

        validationsPassword(updatedNewPassword.getPassword(), updatedNewPassword.getConfirm_password());
        String encryptedNewPassword = encryptPassword(updatedNewPassword.getPassword());
        String encryptedConfirmNewPassword = encryptPassword(updatedNewPassword.getPassword());

        updatedNewPassword.setPassword(encryptedNewPassword);
        updatedNewPassword.setConfirm_password(encryptedConfirmNewPassword);
    }



    // Validations

    public void validationsPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new BusinessRuleException("Passwords do not match.",HttpStatus.BAD_REQUEST);
        }

        if (!isValidPassword(password) && !isValidPassword(confirmPassword)) {
            throw new BusinessRuleException("Password must be at least 8 characters long, include at least one uppercase letter, one lowercase letter, one digit, one special character, and no spaces.", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH
                && password.chars().anyMatch(Character::isUpperCase)
                && password.chars().anyMatch(Character::isLowerCase)
                && password.chars().anyMatch(Character::isDigit)
                && password.chars().anyMatch(ch -> "!@#$%^&*()-_=+[]{}|;:,.<>?/~`".indexOf(ch) >= 0)
                && !password.contains(" ");

    }

    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


}
