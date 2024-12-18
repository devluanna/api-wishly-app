package com.app.service;

import com.app.domain.model.DashboardWishlist.PendingInvitations;
import com.app.domain.model.ResponseDTO.SendInvitationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface SendInvitationService {
    ResponseEntity<?> sendInvitation(SendInvitationDTO sendInvitationDTO, PendingInvitations pendingInvitations);
}
