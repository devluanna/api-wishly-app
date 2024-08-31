package com.app.service.impl;
import com.app.domain.model.ResponseDTO.UpdateUserDTO;
import com.app.domain.model.ResponseDTO.UserDTO;
import com.app.domain.model.Users;
import com.app.domain.repository.UserRepository;
import com.app.exception.BusinessRuleException;
import com.app.service.PasswordService;
import com.app.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PasswordServiceImpl passwordService;


    @Override
    public Users findById(Integer id_user) {
        return userRepository.findById(id_user).orElseThrow(NoSuchElementException::new);
    }
    @Override
    public UserDTO getUserById(Integer id_user) {
        return userRepository.findUserById(id_user);
    }

    public void validatePasswordUser() {

    }

    @Override
    @Transactional
    public UserDTO createNewUser(Users user, UserDTO newUser) {
        userValidationUsername(user);
        userValidationEmail(user);

        passwordService.validationsPassword(user.getPassword(), user.getConfirm_password());
        String encryptedPassword = passwordService.encryptPassword(user.getPassword());
        String encryptedConfirmPassword = passwordService.encryptPassword(user.getPassword());

        Users userCreated = new Users(
                newUser.first_name(),
                newUser.last_name(),
                newUser.email(),
                newUser.username(),
                newUser.date_birthday(),
                newUser.gender(),
                encryptedPassword,
                encryptedConfirmPassword,
                newUser.role(),
                newUser.status()
        );


        userCreated.setPassword(encryptedPassword);
        userCreated.setFirst_name(user.getFirst_name());
        userCreated.setLast_name(user.getLast_name());
        userCreated.setEmail(user.getEmail());
        userCreated.setUsername(user.getUsername());
        userCreated.setDate_birthday(user.getDate_birthday());
        userCreated.setGender(user.getGender());


        Users savedUser = userRepository.save(userCreated);

        return responseRegisterUserDTO(savedUser);

    }

    public UserDTO responseRegisterUserDTO(Users savedUser) {
        UserDTO userDto = new UserDTO(
                savedUser.getId_user(),
                savedUser.getFirst_name(),
                savedUser.getLast_name(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getDate_birthday(),
                savedUser.getGender(),
                savedUser.getPassword(),
                savedUser.getRole(),
                savedUser.getStatus()
        );

        return userDto;

    }

    @Override
    public UpdateUserDTO toUpdateUser(Users userAccount, Integer id_user, UpdateUserDTO updateUserDTO) {

        Users selectedUser = findById(id_user);

        savingNewUpdatesInTheField(userAccount, selectedUser, updateUserDTO);

        Users savedNewInfoUser = userRepository.save(userAccount);

        return responseUpdateUserDTO(savedNewInfoUser);
    }

    public UpdateUserDTO responseUpdateUserDTO(Users savedNewInfoUser) {
        UpdateUserDTO UpdateDTO = new UpdateUserDTO(
                savedNewInfoUser.getId_user(),
                savedNewInfoUser.getFirst_name(),
                savedNewInfoUser.getLast_name(),
                savedNewInfoUser.getEmail(),
                savedNewInfoUser.getUsername(),
                savedNewInfoUser.getDate_birthday(),
                savedNewInfoUser.getGender()
        );

        return UpdateDTO;

    }

    public void selectedUserFieldsDTO(Users selectedUser, UpdateUserDTO updateUserDTO) {
        selectedUser.setFirst_name(updateUserDTO.first_name());
        selectedUser.setLast_name(updateUserDTO.last_name());
        selectedUser.setEmail(updateUserDTO.email());
        selectedUser.setUsername(updateUserDTO.username());
        selectedUser.setDate_birthday(updateUserDTO.date_birthday());
        selectedUser.setGender(updateUserDTO.gender());

    }

    public void savingNewUpdatesInTheField(Users users, Users selectedUser, UpdateUserDTO updateUserDTO) {

        selectedUserFieldsDTO(selectedUser, updateUserDTO);

        if(selectedUser.getFirst_name() != null) {
            users.setFirst_name(selectedUser.getFirst_name());
        }

        if(selectedUser.getLast_name() != null) {
            users.setLast_name(selectedUser.getLast_name());
        }

        if(selectedUser.getEmail() != null) {
            users.setEmail(selectedUser.getEmail());
        }

        if(selectedUser.getUsername() != null) {
            users.setUsername(selectedUser.getUsername());
        }

        if(selectedUser.getDate_birthday() != null) {
            users.setDate_birthday(selectedUser.getDate_birthday());
        }
        if(selectedUser.getGender() != null) {
            users.setGender(selectedUser.getGender());
        }
    }

    // Validations

    private void userValidationEmail(Users user) {
        Users existingEmail = userRepository.findByEmail(user.getEmail());

        if (existingEmail != null) {
            throw new BusinessRuleException("Email already exists.", HttpStatus.CONFLICT);
        }
    }


    private void userValidationUsername(Users user) {
        Users existingUsername = userRepository.findByUsername(user.getUsername());

        if (existingUsername != null) {
            throw new BusinessRuleException("Username already exists! ", HttpStatus.CONFLICT);
        }


        if (!isValidUsername(user.getUsername())) {
            throw new BusinessRuleException("Username must contain at least 3 characters and cannot contain spaces.", HttpStatus.BAD_REQUEST);
        }


    }

    public boolean isValidUsername(String username) {
        if (username.length() < 3) {
            return false;
        }

        if (username.contains(" ")) {
            return false;
        }
        return true;
    }




}
