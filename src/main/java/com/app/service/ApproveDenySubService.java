package com.app.service;
import com.app.domain.model.ResponseDTO.ApproveSubscriberDTO;
import com.app.domain.model.ResponseDTO.CancelRequestDTO;
import com.app.domain.model.ResponseDTO.DenyRequestsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ApproveDenySubService {

    ResponseEntity<?> approveSubscriber(ApproveSubscriberDTO approveSubscriberDTO);

    void denyInvite(DenyRequestsDTO denyRequests);

    void cancelRequests(CancelRequestDTO cancelRequests);
}
