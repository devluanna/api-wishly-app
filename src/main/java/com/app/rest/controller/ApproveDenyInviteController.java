package com.app.rest.controller;

import com.app.domain.model.RequestsByOthers;
import com.app.domain.model.RequestsByYou;
import com.app.domain.model.ResponseDTO.ApproveSubscriberDTO;
import com.app.domain.model.ResponseDTO.CancelRequestDTO;
import com.app.domain.model.ResponseDTO.DenyRequestsDTO;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import com.app.service.ApproveDenyInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invite")
public class ApproveDenyInviteController {


    @Autowired
    ApproveDenyInviteService approveDenyInviteService;

    @PostMapping("/approve")
    public ResponseEntity<?> approveInvite(@RequestBody ApproveSubscriberDTO approveSubscriberDTO) {

        return approveDenyInviteService.approveInvite(approveSubscriberDTO);

    }

    @DeleteMapping("/deny")
    public ResponseEntity denyRequest(@RequestBody DenyRequestsDTO denyRequests) {

        approveDenyInviteService.denyInvite(denyRequests);
        return ResponseEntity.ok("Invite denied successfully!");
    }


    @DeleteMapping("/cancel")
    public ResponseEntity cancelRequest(@RequestBody CancelRequestDTO cancelRequests) {

        approveDenyInviteService.cancelInvite(cancelRequests);
        return ResponseEntity.ok("Invite canceled successfully!");
    }


}
