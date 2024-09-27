package com.app.service;


import com.app.domain.model.ConnectionsDashboard;
import com.app.domain.model.ResponseDTO.ListUsersDTO;
import com.app.domain.model.ResponseDTO.UpdateUserDTO;
import com.app.domain.model.ResponseDTO.UserDTO;
import com.app.domain.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDTO createNewUser(Users user, UserDTO newUser, ConnectionsDashboard connections);
    Users findById(Integer id_user);
    UserDTO getUserById(Integer id_user);
    UpdateUserDTO toUpdateUser(Users userAccount, Integer idUser, UpdateUserDTO updateUserDTO);
    Users disableAccount(Users user, Integer idUser);

    List<ListUsersDTO> getAllUsers();
}
