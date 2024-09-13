package com.app.service.impl;

import com.app.domain.model.*;
import com.app.domain.model.ResponseDTO.ConnectionRequestDTO;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import com.app.domain.repository.*;
import com.app.exception.BusinessRuleException;
import com.app.service.ConnectionsService;
import com.app.utils.AuthenticationUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ConnectionsServiceImpl implements ConnectionsService {

    @Autowired
    ConnectionsDashboardRepository dashboardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestsByOthersRepository requestsByOthersRepository;
    @Autowired
    RequestsByYouRepository requestsByYouRepository;


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

        List<RequestsByYou> existingRequests = existingDashboard.getRequestsByYou();

        if(existingConnections != null || existingRequests != null) {
            for (Connections connections : existingConnections) {
                if (requestDTO.id_user_to_add().equals(connections.getId_user_connection())) {
                    throw new BusinessRuleException("User has already been added.", HttpStatus.BAD_REQUEST);
                }
            }

            for (RequestsByYou requests : existingRequests) {
                if (requestDTO.id_user_to_add().equals(requests.getId_user_to_add())) {
                    throw new BusinessRuleException("There is already a pending request for the user", HttpStatus.BAD_REQUEST);
                }
            }
        }
    }


    public RequestsByYou requestConnection(ConnectionRequestDTO requestDTO, RequestsByYou requestsByYou, RequestsByOthers requestsByOthers) {

        Users authenticatedUser = userValidation(requestDTO);

        ConnectionsDashboard dashboard = dashboardRepository.findById(requestDTO.id_dashboard())
                .orElseThrow(() -> new EntityNotFoundException("Dashboard not found"));

        Users userToAdd = userRepository.findById(requestDTO.id_user_to_add())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

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

    @Override
    public Connections removeConnection(UpdateRequestDTO responseRequestDTO, Connections connections) {
        return null;
    }



}
