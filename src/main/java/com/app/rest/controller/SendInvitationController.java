package com.app.rest.controller;

import com.app.domain.model.DashboardWishlist.PendingInvitations;
import com.app.domain.model.ResponseDTO.SendInvitationDTO;
import com.app.service.SendInvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invitation")
public class SendInvitationController {

    @Autowired
    SendInvitationService sendInvitationService;

    @PostMapping("/send")
    public ResponseEntity<?> sendInvitationWishlist(@RequestBody SendInvitationDTO sendInvitationDTO, PendingInvitations pendingInvitations) {

        return sendInvitationService.sendInvitation(sendInvitationDTO, pendingInvitations);

    }



}
