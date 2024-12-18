package com.app.service.impl;

import com.app.domain.model.*;
import com.app.domain.model.DashboardWishlist.PendingInvitations;
import com.app.domain.model.DashboardWishlist.SubscriberRequests;
import com.app.domain.model.ResponseDTO.UpdateRequestDTO;
import com.app.domain.model.Utilities.Pending;
import com.app.domain.model.Utilities.Requests;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.model.Wishlist.WishlistSubscribers;
import com.app.domain.repository.ConnectionsUser.RequestsByOthersRepository;
import com.app.domain.repository.ConnectionsUser.RequestsByYouRepository;
import com.app.domain.repository.User.ConnectionsDashboardRepository;
import com.app.domain.repository.ConnectionsUser.ConnectionsRepository;
import com.app.domain.repository.User.NotificationUserRepository;
import com.app.domain.repository.User.UserRepository;
import com.app.domain.repository.Wishlist.*;
import com.app.service.ApproveDenyConnectionService;
import com.app.utils.AuthenticationUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
    NotificationUserRepository notificationsUserRepository;
    @Autowired
    RequestsByOthersRepository requestsByOthersRepository;
    @Autowired
    RequestsByYouRepository requestsByYouRepository;

    @Autowired
    NotificationServiceImpl notificationServiceImpl;

    @Autowired
    PendingInvitationsRepository pendingInvitationsRepository;

    @Autowired
    PendingRepository pendingRepository;

    @Autowired
    MySubscriberRequestsRepository mySubscriberRequestsRepository;

    @Autowired
    SubscribersRequestsRepository subscribersRequestsRepository;

    @Autowired
    WishlistSubscribersRepository wishlistSubscribersRepository;

    @Autowired
    WishlistRepository wishlistRepository;

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

        changeStatusConnection(requesterId, authenticatedUser);

        notificationTheUser(requesterId, authenticatedUser);

        return requestExist;
    }


    private void changeStatusConnection(Integer requesterId, Users authenticatedUser) {
        changeStatusConnectionsSubscribers(requesterId, authenticatedUser);
        changeStatusConnectionsPending(requesterId, authenticatedUser);
        changeStatusConnectionsPendingInvitation(requesterId, authenticatedUser);
        changeStatusConnectionsRequestsWishlist(requesterId, authenticatedUser);
        changeStatusConnectionsMyRequestsSubscriptions(requesterId, authenticatedUser);

    }

    private void changeStatusConnectionsSubscribers(Integer requesterId, Users authenticatedUser) {
        List<WishlistSubscribers> existSubscribers = wishlistSubscribersRepository.findAll();

        for (WishlistSubscribers subscriber : existSubscribers) {

            if (subscriber.getId_user().equals(requesterId)
                    && subscriber.getWishlist().getId_owner().equals(authenticatedUser.getId_user())) {
                subscriber.setUserWithConnection(true);
            }

            else if (subscriber.getId_user().equals(authenticatedUser.getId_user())
                    && subscriber.getWishlist().getId_owner().equals(requesterId)) {
                subscriber.setUserWithConnection(true);
            }
        }


        wishlistSubscribersRepository.saveAll(existSubscribers);
    }

    private void changeStatusConnectionsPending(Integer requesterId, Users authenticatedUser) {
        List<Pending> existPending = pendingRepository.findAll();

        for (Pending pending : existPending) {

            if (pending.getId_user_guest().equals(requesterId)
                    && pending.getId_owner_user().equals(authenticatedUser.getId_user())) {
                pending.setUserWithConnection(true);
            }

            else if (pending.getId_user_guest().equals(authenticatedUser.getId_user())
                    && pending.getId_owner_user().equals(requesterId)) {
                pending.setUserWithConnection(true);
            }
        }

        pendingRepository.saveAll(existPending);
    }

    private void changeStatusConnectionsPendingInvitation(Integer requesterId, Users authenticatedUser) {
        List<PendingInvitations> existPending = pendingInvitationsRepository.findAll();

        for (PendingInvitations invitationsPending : existPending) {

            if (invitationsPending.getId_user_guest().equals(requesterId)
                    && invitationsPending.getId_owner().equals(authenticatedUser.getId_user())) {
                invitationsPending.setUserWithConnection(true);
            }

            else if (invitationsPending.getId_user_guest().equals(authenticatedUser.getId_user())
                    && invitationsPending.getId_owner().equals(requesterId)) {
                invitationsPending.setUserWithConnection(true);
            }
        }

        pendingInvitationsRepository.saveAll(existPending);
    }

    private void changeStatusConnectionsRequestsWishlist(Integer requesterId, Users authenticatedUser) {
        List<SubscriberRequests> existRequestsWishlist = subscribersRequestsRepository.findAll();

        for (SubscriberRequests requests : existRequestsWishlist) {

            Integer idOwnerWishlist = requests.getId_wishlist();

            Wishlist existWishlist = wishlistRepository.findById(idOwnerWishlist)
                    .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));

            Integer getOwner = existWishlist.getDashboardWishlists().getId_responsible_user();

            if (requests.getId_user().equals(requesterId)
                    && getOwner.equals(authenticatedUser.getId_user())) {
                requests.setUserWithConnection(true);
            }

            else if (requests.getId_user().equals(authenticatedUser.getId_user())
                    && getOwner.equals(requesterId)) {
                requests.setUserWithConnection(true);
            }
        }

        subscribersRequestsRepository.saveAll(existRequestsWishlist);
    }

    private void changeStatusConnectionsMyRequestsSubscriptions(Integer requesterId, Users authenticatedUser) {
        List<Requests> existMyRequests = mySubscriberRequestsRepository.findAll();

        for (Requests myRequests : existMyRequests) {

            if (myRequests.getId_user().equals(requesterId)
                    && myRequests.getId_owner_user().equals(authenticatedUser.getId_user())) {
                myRequests.setUserWithConnection(true);
            }

            else if (myRequests.getId_user().equals(authenticatedUser.getId_user())
                    && myRequests.getId_owner_user().equals(requesterId)) {
                myRequests.setUserWithConnection(true);
            }
        }

        mySubscriberRequestsRepository.saveAll(existMyRequests);
    }

    public void notificationTheUser(Integer requesterId, Users authenticatedUser) {
        Users user = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String nameResponsibleApproval = authenticatedUser.getFirst_name() + " " + authenticatedUser.getLast_name();
        String notificationDescription = "Your connection request to the user " + nameResponsibleApproval + " was successfully approved. You are now friends!";

        notificationServiceImpl.sendNotification(
                requesterId,
                "Connection Request Approved!",
                notificationDescription,
                false
        );
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
                StatusConnections.APPROVEDBYOTHERS,
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

        Connections savedConnections = connectionsRepository.save(completionRequest);
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
