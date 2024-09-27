package com.app.rest.controller;

import com.app.domain.model.ResponseDTO.ListUsersDTO;
import com.app.domain.model.ResponseDTO.*;
import com.app.domain.model.Users;
import com.app.service.PasswordService;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserManagerController {


    @Autowired
    UserService userService;

    @Autowired
    PasswordService passwordService;


    @GetMapping("/u/{id_user}")
    public ResponseEntity <UserDTO> getUserById(@PathVariable Integer id_user) {
        UserDTO personUser = userService.getUserById(id_user);

        if(personUser == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personUser);
    }

    @GetMapping("/list-users")
    public ResponseEntity<List<ListUsersDTO>> getAllUsers() {

        List<ListUsersDTO> user = userService.getAllUsers();

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }


    @PutMapping("/{id_user}")
    public ResponseEntity updateUser(@PathVariable Integer id_user, @RequestBody UpdateUserDTO updateUserDTO) {

        if (id_user == null) {
            System.out.println("User not found!");
            return ResponseEntity.badRequest().build();
        }

        Users account = userService.findById(id_user);

        UpdateUserDTO updatedAccount = userService.toUpdateUser(account, id_user, updateUserDTO);

        return ResponseEntity.ok(updatedAccount);
    }

    @PutMapping("/password/{id_user}")
    public ResponseEntity updatePassword(@PathVariable Integer id_user, @RequestBody PasswordDTO passwordDTO) {
        try {
            Users user = userService.findById(id_user);
            user.setPassword(passwordDTO.password());
            user.setConfirm_password(passwordDTO.confirm_password());

            passwordService.toUpdatePassword(user, passwordDTO, id_user);
            return ResponseEntity.ok("Password updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody AccountRecoveryDTO recoveryDTO, String email, Users user) {

        passwordService.recoveryPassword(user, recoveryDTO, email);

        return ResponseEntity.ok("Please check your email. Password Recovery Token has been sent!");
    }

    @PutMapping("/disable/{id_user}")
    public ResponseEntity disableAccount(@PathVariable Integer id_user) {
        try {
            Users user = userService.findById(id_user);

            userService.disableAccount(user, id_user);
            return ResponseEntity.ok("Account successfully deactivated!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }





}
