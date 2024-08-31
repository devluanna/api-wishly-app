package com.app.service.impl;

import com.app.domain.model.ResponseDTO.PasswordDTO;
import com.app.domain.model.ResponseDTO.RecoveryPasswordDTO;
import com.app.domain.model.ResponseDTO.UserDTO;
import com.app.domain.model.Users;
import com.app.domain.repository.UserRepository;
import com.app.exception.BusinessRuleException;
import com.app.service.PasswordService;
import com.app.service.UserService;
import com.app.utils.MailConfig;
import jakarta.transaction.Transactional;
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
    MailConfig emailService;
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users recoveryPassword(Users user, RecoveryPasswordDTO recoveryDTO, String email) {

        Users existingUser = usersRepository.findByEmail(recoveryDTO.email());

        if (existingUser != null) {
            String tokenRecoveryUser = generateTokenRecovery();

            String encryptedPassword = passwordEncoder.encode(tokenRecoveryUser);
            existingUser.setCode_recovery_password(encryptedPassword);
            usersRepository.save(existingUser);

            usersRepository.save(existingUser);

            //SendingTheRecoveryEmail(existingUser, tokenRecoveryUser);

            return existingUser;
        } else {
            throw new BusinessRuleException("Email does not match any user's email.", HttpStatus.BAD_REQUEST);
        }
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



    private void SendingTheRecoveryEmail(UserDTO users, Users savedUser, String tokenRecoveryUser) {
        String subject = "Phew!!! It's so good to see you here again :')";
        String emailBody = "Hello! " + users.first_name() + " " + users.last_name() + ",\n\nWelcome back!\n\n" +
                "\n\nRemember to reset your password and make your profile as updated and complete as possible.\n\n" +
                "Here is your information for recovering and updating your new password.\n\n" +
                "Login identity: " + savedUser.getUsername() + "\n" +
                "Token for recovery: " + tokenRecoveryUser;

        emailService.sendEmail(users.email(), subject, emailBody);
    }


    @Transactional
    private String generateTokenRecovery() {

        String characters = "0123456789";
        StringBuilder identity = new StringBuilder();
        int length = 6;

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            identity.append(characters.charAt(index));
        }

        return identity.toString();
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
