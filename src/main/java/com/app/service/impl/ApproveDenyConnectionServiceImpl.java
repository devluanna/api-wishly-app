package com.app.service.impl;

import com.app.domain.model.*;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import com.app.domain.repository.*;
import com.app.exception.BusinessRuleException;
import com.app.service.ApproveDenyConnectionService;
import com.app.utils.AuthenticationUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ApproveDenyConnectionServiceImpl implements ApproveDenyConnectionService {

    //REFATORAR OS METODOS DE APROVACAO!
    // DELETAR UM USUARIO EXISTENTE NA LISTA DE CONEXOES

    @Autowired
    ConnectionsRepository connectionsRepository;
    @Autowired
    ConnectionsDashboardRepository dashboardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestsByOthersRepository requestsByOthersRepository;
    @Autowired
    RequestsByYouRepository requestsByYouRepository;

    public String getUserFullName(Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getFirst_name() + " " + user.getLast_name();
    }


    @Transactional
    public RequestsByOthers approveConnectionRequest(UpdateRequestDTO responseRequestDTO, Connections connections, RequestsByOthers requestsByOthers, RequestsByYou requestsByYou) {

        Users authenticatedUser = AuthenticationUtils.validateUser(() -> responseRequestDTO.approval_user_id());

        ConnectionsDashboard dashboardUser = authenticatedUser.getConnectionsDashboard();

        List<RequestsByOthers> existingConnectionsRequests = dashboardUser.getRequestsByOthers();

        RequestsByOthers requestExist = existingConnectionsRequests.stream()
                .filter(req -> req.getId_user_requestor().equals(responseRequestDTO.id_user_requester()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        requestExist.setStatusConnections(StatusConnections.APPROVEDBYME);

        Integer requesterId = responseRequestDTO.id_user_requester();
        String name = getUserFullName(requestExist.getId_user_requestor());

        completeConnectionRequest(
                requestExist.getId_user_requestor(),
                name,
                requestExist.getUsername(),
                StatusConnections.APPROVEDBYME,
                dashboardUser,
                connections
        );


        approveConnectionForRequester(responseRequestDTO, connections, requesterId);

        requestsByOthersRepository.deleteByIdRequestsPending(requestExist.getId_requests_pending());
        dashboardRepository.save(dashboardUser);

        return requestExist;
    }


    @Transactional
    public void approveConnectionForRequester(UpdateRequestDTO responseRequestDTO, Connections connections, Integer requesterId) {
        Users userExists = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ConnectionsDashboard dashboardUserRequestor = userExists.getConnectionsDashboard();

        List<RequestsByYou> existingRequestsByRequestor = userExists.getConnectionsDashboard().getRequestsByYou();

        RequestsByYou requestExist = existingRequestsByRequestor.stream()
                .filter(req -> req.getId_user_to_add().equals(responseRequestDTO.approval_user_id()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        requestsByYouRepository.deleteByIdRequestsPending(requestExist.getId_requests());

        String name = getUserFullName(requestExist.getId_user_to_add());

        completeConnectionRequest(
                requestExist.getId_user_to_add(),
                name,
                requestExist.getUsername(),
                StatusConnections.APPROVEDBYREQUESTED,
                dashboardUserRequestor,
                connections
        );
    }

    @Transactional
    public void completeConnectionRequest(
            Integer userId,
            String name,
            String userUsername,
            StatusConnections statusConnections,
            ConnectionsDashboard dashboardUser,
            Connections connections) {

        Connections completionRequest = new Connections();
        completionRequest.setId_user_connection(userId);
        completionRequest.setName(name);
        completionRequest.setUsername(userUsername);
        completionRequest.setStatusConnections(statusConnections);
        completionRequest.setConnection_date(new Date());
        completionRequest.setProfileIsOpenForConnections(true);
        completionRequest.setDashboard(dashboardUser);

        dashboardUser.addNewConnection(completionRequest);
        connections.setDashboard(dashboardUser);

        connectionsRepository.save(completionRequest);
        dashboardRepository.save(dashboardUser);


    }

    @Override
    @Transactional
    public void denyConnectionRequest(UpdateRequestDTO responseRequestDTO, RequestsByOthers requestsByOthers, RequestsByYou requestsByYou) {
        Users authenticatedUser = AuthenticationUtils.validateUser(() -> responseRequestDTO.approval_user_id());

        ConnectionsDashboard dashboardUser = authenticatedUser.getConnectionsDashboard();

        removeRequestFromRequestsByOthers(responseRequestDTO.id_user_requester(), dashboardUser);

        removeRequestFromRequestsByYou(responseRequestDTO.approval_user_id(), responseRequestDTO.id_user_requester());

    }
    @Transactional
    private void removeRequestFromRequestsByOthers(Integer requesterId, ConnectionsDashboard dashboardUser) {
        List<RequestsByOthers> existingConnectionsRequests = dashboardUser.getRequestsByOthers();

        RequestsByOthers requestExist = existingConnectionsRequests.stream()
                .filter(req -> req.getId_user_requestor().equals(requesterId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Request not found in RequestsByOthers"));


        existingConnectionsRequests.remove(requestExist);


        dashboardRepository.save(dashboardUser);
        requestsByOthersRepository.delete(requestExist);
    }
    @Transactional
    private void removeRequestFromRequestsByYou(Integer approverId, Integer requesterId) {
        Users userExists = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ConnectionsDashboard dashboardUserRequestor = userExists.getConnectionsDashboard();

        List<RequestsByYou> existingRequestsByRequestor = userExists.getConnectionsDashboard().getRequestsByYou();

        RequestsByYou requestExist = existingRequestsByRequestor.stream()
                .filter(req -> req.getId_user_to_add().equals(approverId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        requestsByYouRepository.deleteByIdRequestsPending(requestExist.getId_requests());
        dashboardRepository.save(dashboardUserRequestor);
    }


}
