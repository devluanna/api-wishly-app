package com.app.rest.controller;

import com.app.domain.model.ConnectionsDashboard;
import com.app.domain.model.ResponseDTO.AuthenticationDTO;
import com.app.domain.model.ResponseDTO.ResponseTokenDTO;
import com.app.domain.model.ResponseDTO.UserDTO;
import com.app.domain.model.Users;
import com.app.infra.security.TokenService;
import com.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager manager;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO oauth, Users user) {
        var auth = new UsernamePasswordAuthenticationToken(oauth.username(), oauth.password());
        var authentication = manager.authenticate(auth);

        var token = tokenService.generateToken((Users) authentication.getPrincipal());

        Users authenticatedUser = (Users) authentication.getPrincipal();
        Integer userId = authenticatedUser.getId_user();


        return ResponseEntity.ok(new ResponseTokenDTO(token, userId));
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody Users user, UserDTO newUser, ConnectionsDashboard connections) {

        UserDTO userCreated = userService.createNewUser(user, newUser, connections);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

    }




}
