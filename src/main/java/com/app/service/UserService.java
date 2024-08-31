package com.app.service;

import com.app.domain.model.ResponseDTO.UpdateUserDTO;
import com.app.domain.model.ResponseDTO.UserDTO;
import com.app.domain.model.Users;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDTO createNewUser(Users user, UserDTO newUser);

    Users findById(Integer id_user);
    UserDTO getUserById(Integer id_user);

    UpdateUserDTO toUpdateUser(Users userAccount, Integer idUser, UpdateUserDTO updateUserDTO);
}
