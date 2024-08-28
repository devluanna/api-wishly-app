package com.app.service.impl;

import com.app.domain.model.ResponseDTO.UserDTO;
import com.app.domain.model.User;
import com.app.domain.repository.UserRepository;
import com.app.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User findById(Integer id_user) {
        return userRepository.findById(id_user).orElseThrow(NoSuchElementException::new);
    }
    @Override
    public UserDTO getUserById(Integer id_user) {
        return userRepository.findUserById(id_user);
    }

    @Override
    @Transactional
    public UserDTO createNewUser(UserDTO newUser, User user) {

        

    }

}
