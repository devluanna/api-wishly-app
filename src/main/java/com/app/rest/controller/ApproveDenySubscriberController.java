package com.app.rest.controller;

import com.app.domain.model.ResponseDTO.ApproveSubscriberDTO;
import com.app.domain.model.ResponseDTO.CancelRequestDTO;
import com.app.domain.model.ResponseDTO.DenyRequestsDTO;
import com.app.service.ApproveDenySubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/request/subscriber")
public class ApproveDenySubscriberController {

    @Autowired
    ApproveDenySubService approveDenySubService;

    @PutMapping("/approve")
    public ResponseEntity<?> approveSubscriber(@RequestBody ApproveSubscriberDTO approveSubscriberDTO) {

        return approveDenySubService.approveSubscriber(approveSubscriberDTO);

    }

    @DeleteMapping("/deny")
    public ResponseEntity denyRequest(@RequestBody DenyRequestsDTO denyRequests) {

        approveDenySubService.denyInvite(denyRequests);
        return ResponseEntity.ok("Invite denied successfully!");
    }


    @DeleteMapping("/cancel")
    public ResponseEntity cancelRequest(@RequestBody CancelRequestDTO cancelRequests) {

        approveDenySubService.cancelRequests(cancelRequests);
        return ResponseEntity.ok("Invite canceled successfully!");
    }

}
