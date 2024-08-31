package com.app.rest.controller;

import com.app.domain.model.ResponseDTO.AccountRecoveryDTO;
import com.app.domain.model.ResponseDTO.ActiveAccountDTO;
import com.app.domain.model.Users;
import com.app.service.ActivateAccountService;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reset-account")
public class ActivateAccountController {

    @Autowired
    ActivateAccountService activateAccountService;

    @Autowired
    UserService userService;


    @PostMapping("/recovery-account")
    public ResponseEntity<String> recoveryAccount(@RequestBody AccountRecoveryDTO accountDTO, String email, Users user) {

        activateAccountService.recoveryAccount(user, accountDTO, email);

        return ResponseEntity.ok("Please check your email. Password Recovery Token has been sent!");
    }

    @PutMapping("/activate-account")
    public ResponseEntity activateAccount(@RequestParam String email, @RequestBody Users user, ActiveAccountDTO activeAccountDTO) {

        ActiveAccountDTO accountActivated = activateAccountService.activateAccount(user, activeAccountDTO);

        return ResponseEntity.ok(accountActivated);
    }

}
