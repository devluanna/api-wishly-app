package com.app.service;

import com.app.domain.model.ResponseDTO.ApproveSubscriberDTO;
import com.app.domain.model.ResponseDTO.CancelRequestDTO;
import com.app.domain.model.ResponseDTO.DenyRequestsDTO;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ApproveDenyInviteService {
    ResponseEntity<?> approveInvite(ApproveSubscriberDTO approveSubscriberDTO);

    void denyInvite(DenyRequestsDTO denyRequests);

    void cancelInvite(CancelRequestDTO cancelRequests);
}
