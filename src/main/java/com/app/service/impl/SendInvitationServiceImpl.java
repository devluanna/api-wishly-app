package com.app.service.impl;
import com.app.domain.model.Connections;
import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import com.app.domain.model.DashboardWishlist.PendingInvitations;
import com.app.domain.model.DashboardWishlist.SubscriberRequests;
import com.app.domain.model.ResponseDTO.SendInvitationDTO;
import com.app.domain.model.ResponseDTO.SuccessResponse;
import com.app.domain.model.Users;
import com.app.domain.model.Utilities.DashboardRequestsAndPending;
import com.app.domain.model.Utilities.MySubscriptions;
import com.app.domain.model.Utilities.Pending;
import com.app.domain.model.Utilities.SubscriptionType;
import com.app.domain.model.Wishlist.*;
import com.app.domain.repository.User.UserRepository;
import com.app.domain.repository.Wishlist.*;
import com.app.exception.BusinessRuleException;
import com.app.service.SendInvitationService;
import com.app.utils.AuthenticationUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class SendInvitationServiceImpl implements SendInvitationService {

    @Autowired
    PendingInvitationsRepository pendingRepository;

    @Autowired
    DashboardRequestsSubscribersRepository dashboardRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DashboardRequestsAndPendingRepository dashboardRequestsUsers;

    @Autowired
    PendingRepository invitesUserPendingRepository;

    @Override
    public ResponseEntity<?> sendInvitation(SendInvitationDTO sendInvitationDTO, PendingInvitations pendingInvitations) {
       Users authenticateUser = AuthenticationUtils.getAuthenticatedUser();
       AuthenticationUtils.validateUser(() -> sendInvitationDTO.id_owner());

       Wishlist existWishlist = findWishlist(sendInvitationDTO.wishlist_invitation());

       if(existWishlist != null) {
           if (existWishlist.getVisibility().equals(Visibility.PRIVATE.getVisibility_description())) {
               newInviteSend(existWishlist, sendInvitationDTO);
               return ResponseEntity.ok(new SuccessResponse("Invitation sent successfully!"));
           } else {
               throw new BusinessRuleException("The wishlist you are trying to send an invitation to is not a PRIVATE wishlist",
                       HttpStatus.BAD_REQUEST);
           }

       } else {
           throw new BusinessRuleException("Wishlist not found!", HttpStatus.BAD_REQUEST);
       }
    }


    private Wishlist findWishlist(Integer idWishlist) {
        return wishlistRepository.findById(idWishlist)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
    }

    private void validateUserExist(Wishlist existWishlist, Integer selectedUserId) {
        validateExistInvite(existWishlist,selectedUserId);
        validateExistSubscriber(existWishlist,selectedUserId);
        validateExistRequestPending(existWishlist,selectedUserId);

    }

    private void validateExistInvite(Wishlist existWishlist, Integer selectedUserId) {

        List<PendingInvitations> existInvitePending = existWishlist.getDashboardRequestsSubscribers().getPendingInvitations();

        for(PendingInvitations invite : existInvitePending) {
            if(invite.getId_user_guest().equals(selectedUserId)) {
                throw new BusinessRuleException("Invitation already sent, await approval", HttpStatus.BAD_REQUEST);
            }
        }

    }


    private void validateExistSubscriber(Wishlist existWishlist, Integer selectedUserId) {

        List<WishlistSubscribers> existSubscriber = existWishlist.getWishlistSubscribers();
        for(WishlistSubscribers sub : existSubscriber) {
            if(sub.getId_user().equals(selectedUserId)) {
                throw new BusinessRuleException("It is not possible to send the invitation because the user is already registered in the desired Wishlist.",
                        HttpStatus.BAD_REQUEST);
            }
        }

    }

    private void validateExistRequestPending(Wishlist existWishlist, Integer selectedUserId) {

        List<SubscriberRequests> existSubscriber = existWishlist.getDashboardRequestsSubscribers().getSubscriberRequests();
        for(SubscriberRequests req : existSubscriber) {
            if(req.getId_user().equals(selectedUserId)) {
                throw new BusinessRuleException("It is not possible to make the invitation, the user has an open request pending approval.",
                        HttpStatus.BAD_REQUEST);
            }
        }

    }



    private void newInviteSend(Wishlist existWishlist,
                              SendInvitationDTO sendInvitationDTO) {

        Integer selectedUserId = sendInvitationDTO.id_user_guest();

        Users existUser = userRepository.findById(selectedUserId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        DashboardRequestsSubscribers selectedDashboardRequests = existWishlist.getDashboardRequestsSubscribers();

        validateUserExist(existWishlist,selectedUserId);

        if(existUser != null) {

            PendingInvitations newInvite = new PendingInvitations();

            validateConnections(existWishlist, sendInvitationDTO, newInvite);

            newInvite.setId_user_guest(existUser.getId_user());
            newInvite.setUsername_guest(existUser.getUsername());
            newInvite.setInvitation_date(new Date());
            newInvite.setStatusSubscribers(StatusSubscribers.valueOf("PENDINGGUESTAPPROVAL"));

            newInvite.setId_wishlist(existWishlist.getId_wishlist());
            newInvite.setName_wishlist(existWishlist.getWishlist_name());
            newInvite.setId_owner(existWishlist.getId_owner());
            newInvite.setDashboard_pending_subscribers(selectedDashboardRequests);

            selectedDashboardRequests.addPendingInvitations(newInvite);
            selectedDashboardRequests.setCount_pending_invitations(selectedDashboardRequests.getPendingInvitations().size());

            pendingRepository.save(newInvite);
            dashboardRepository.save(selectedDashboardRequests);

            addNewInviteDashboardUser(existWishlist,newInvite, existUser);
        }
    }

    public void addNewInviteDashboardUser(Wishlist existWishlist, PendingInvitations newInvite, Users existUser) {

        DashboardRequestsAndPending selectedDashboardSubsUser = existUser.getDashboardRequestsAndPending();

        Pending newGuest = new Pending();
        newGuest.setId_user_guest(existUser.getId_user());
        newGuest.setUsername_guest(existUser.getUsername());
        newGuest.setId_owner_user(existWishlist.getId_owner());
        newGuest.setUsername_owner(existWishlist.getUsername_owner());
        newGuest.setId_wishlist(existWishlist.getId_wishlist());
        newGuest.setWishlist_name(existWishlist.getWishlist_name());
        newGuest.setDate_invited(newInvite.getInvitation_date());
        newGuest.setStatusSubscribers(StatusSubscribers.valueOf("PENDINGGUESTAPPROVAL"));
        newGuest.setUserWithConnection(newInvite.isUserWithConnection());
        newGuest.setDashboard_requests_pending(selectedDashboardSubsUser);

        selectedDashboardSubsUser.addInvite(newGuest);
        selectedDashboardSubsUser.setCount_pending(selectedDashboardSubsUser.getPending().size());

        invitesUserPendingRepository.save(newGuest);
        dashboardRequestsUsers.save(selectedDashboardSubsUser);


    }

    private void validateConnections(Wishlist existWishlist,SendInvitationDTO sendInvitationDTO, PendingInvitations newInvite) {
        Integer idOwner = existWishlist.getId_owner();

        Users existUserOwner = userRepository.findById(idOwner).orElseThrow(() -> new EntityNotFoundException("User not Found!"));

        if(existUserOwner != null) {
            List<Connections> listConnections = existUserOwner.getConnectionsDashboard().getConnections();

            for(Connections conn : listConnections ) {
                if(sendInvitationDTO.id_user_guest().equals(conn.getId_user_connection())) {
                    newInvite.setUserWithConnection(true);
                }
            }
        } else {
            throw new BusinessRuleException("User not found", HttpStatus.BAD_REQUEST);
        }
    }


}
