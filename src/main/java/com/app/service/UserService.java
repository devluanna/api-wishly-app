package com.app.service;

import com.app.domain.model.ResponseDTO.UserDTO;
import com.app.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDTO createNewUser(UserDTO newUser, User user);

    User findById(Integer id_user);
    UserDTO getUserById(Integer id_user);
}
