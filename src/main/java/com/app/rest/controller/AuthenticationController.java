package com.app.rest.controller;

import com.app.domain.model.ResponseDTO.AuthenticationDTO;
import com.app.domain.model.ResponseDTO.ResponseTokenDTO;
import com.app.domain.model.User;
import com.app.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO oauth, User user) {
        var auth = new UsernamePasswordAuthenticationToken(oauth.username(), oauth.password());
        var authentication = manager.authenticate(auth);

        var token = tokenService.generateToken((User) authentication.getPrincipal());

        User authenticatedUser = (User) authentication.getPrincipal();
        Integer userId = authenticatedUser.getId_user();


        return ResponseEntity.ok(new ResponseTokenDTO(token, userId));



    }




}
