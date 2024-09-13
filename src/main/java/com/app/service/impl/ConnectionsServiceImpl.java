package com.app.service.impl;

import com.app.domain.model.*;
import com.app.domain.model.ResponseDTO.ConnectionRequestDTO;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import com.app.domain.repository.*;
import com.app.exception.BusinessRuleException;
import com.app.service.ConnectionsService;
import com.app.utils.AuthenticationUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionsServiceImpl implements ConnectionsService {


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


    //DELETAR DA TABELA DE PENDENCIAS DE QUEM APROVOU - ok
    //ADICIONAR NA TABELA DE CONNECTIONS DE QUEM SOLICITOU - ok
    //DELETAR DA TABELA DE PENDENCIAS DE QUEM ADICIONOU - ok
    private static Users userValidation(ConnectionRequestDTO requestDTO) {
        Users authenticatedUser = AuthenticationUtils.validateUser(() -> requestDTO.id_user_requester());

        if(requestDTO.id_user_to_add().equals(authenticatedUser.getId_user())) {
            throw new BusinessRuleException("This function is not allowed.", HttpStatus.BAD_REQUEST);
        }

        if(!requestDTO.id_dashboard().equals(authenticatedUser.getConnectionsDashboard().getId_dashboard())) {
            throw new BusinessRuleException("This function is not allowed.", HttpStatus.BAD_REQUEST);
        }
        return authenticatedUser;
    }


    private void isMemberAlreadyInSquad(Users authenticatedUser, ConnectionRequestDTO requestDTO) {

        ConnectionsDashboard existingDashboard = authenticatedUser.getConnectionsDashboard();

        List<Connections> existingConnections = existingDashboard.getConnections();

        if(existingConnections != null) {
            for (Connections connections : existingConnections) {
                if (requestDTO.id_user_to_add().equals(connections.getId_user_connection())) {
                    throw new BusinessRuleException("User has already been added.", HttpStatus.BAD_REQUEST);
                }
            }
        }
    }


    public RequestsByYou requestConnection(ConnectionRequestDTO requestDTO, RequestsByYou requestsByYou, RequestsByOthers requestsByOthers) {

        Users authenticatedUser = userValidation(requestDTO);

        ConnectionsDashboard dashboard = dashboardRepository.findById(requestDTO.id_dashboard())
                .orElseThrow(() -> new EntityNotFoundException("Dashboard not found"));

        Users userToAdd = userRepository.findById(requestDTO.id_user_to_add())
                .orElseThrow(() -> new EntityNotFoundException("Usuário not found"));

        isMemberAlreadyInSquad(authenticatedUser, requestDTO);

        ConnectionsDashboard dashboardRequested = userToAdd.getConnectionsDashboard();

        RequestsByYou newConnection = requesterConnection(userToAdd, dashboard, requestsByYou);

        requestedConnection(authenticatedUser, dashboardRequested, requestsByOthers);

        return newConnection;
    }



    private RequestsByYou requesterConnection(Users userToAdd, ConnectionsDashboard dashboard, RequestsByYou requestsByYou) {

        RequestsByYou newRequest = new RequestsByYou();
        newRequest.setUsername(userToAdd.getUsername());
        newRequest.setId_user_to_add(userToAdd.getId_user());
        newRequest.setId_dashboard_user_to_add(userToAdd.getConnectionsDashboard().getId_dashboard());
        newRequest.setStatusConnections(StatusConnections.ONSTAND);
        newRequest.setConnection_date(new Date());
        newRequest.setDashboardRequester(dashboard);

        dashboard.addNewRequest(newRequest);
        requestsByYou.setDashboardRequester(dashboard);

        requestsByYouRepository.save(newRequest);
        dashboardRepository.save(dashboard);

        return newRequest;
    }

    private void requestedConnection(Users requesterUser, ConnectionsDashboard dashboardRequested, RequestsByOthers requestsByOthers) {

        RequestsByOthers newConnectionRequested = new RequestsByOthers();
        newConnectionRequested.setId_user_requestor(requesterUser.getId_user());
        newConnectionRequested.setId_dashboard_user_requestor(requesterUser.getConnectionsDashboard().getId_dashboard());
        newConnectionRequested.setUsername(requesterUser.getUsername());
        newConnectionRequested.setConnection_date(new Date());
        newConnectionRequested.setStatusConnections(StatusConnections.ONSTAND);
        newConnectionRequested.setDashboard(dashboardRequested);

        dashboardRequested.addNewRequested(newConnectionRequested);
        requestsByOthers.setDashboard(dashboardRequested);

        requestsByOthersRepository.save(newConnectionRequested);
        dashboardRepository.save(dashboardRequested);

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
                dashboardUserRequestor,
                connections
        );


    }
    public String getUserFullName(Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getFirst_name() + " " + user.getLast_name();
    }


    @Transactional
    public void completeConnectionRequest(
            Integer userId,
            String name,
            String userUsername,
            ConnectionsDashboard dashboardUser,
            Connections connections) {

        Connections completionRequest = new Connections();
        completionRequest.setId_user_connection(userId);
        completionRequest.setName(name);
        completionRequest.setUsername(userUsername);
        completionRequest.setStatusConnections(StatusConnections.APPROVEDBYME);
        completionRequest.setConnection_date(new Date());
        completionRequest.setProfileIsOpenForConnections(true);
        completionRequest.setDashboard(dashboardUser);

        dashboardUser.addNewConnection(completionRequest);
        connections.setDashboard(dashboardUser);

        connectionsRepository.save(completionRequest);
        dashboardRepository.save(dashboardUser);
    }



    public void countConnectionsTotal(Integer id_dashboard) {

        Optional<ConnectionsDashboard> dashboardOptional = dashboardRepository.findById(id_dashboard);

        if (dashboardOptional.isPresent()) {

            ConnectionsDashboard dashboard = dashboardOptional.get();

            int totalConnections = dashboard.getConnections().size();

            dashboard.setCount_friends(totalConnections + 1);
            dashboardRepository.save(dashboard);

            System.out.println("Total de conexões atualizadas: " + totalConnections);

        } else {
            throw new BusinessRuleException("Dashboard not found", HttpStatus.BAD_REQUEST);
        }

    }
}
