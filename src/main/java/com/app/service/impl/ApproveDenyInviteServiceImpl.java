package com.app.service.impl;

import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import com.app.domain.model.DashboardWishlist.PendingInvitations;
import com.app.domain.model.DashboardWishlist.SubscriberRequests;
import com.app.domain.model.ResponseDTO.ApproveSubscriberDTO;
import com.app.domain.model.ResponseDTO.CancelRequestDTO;
import com.app.domain.model.ResponseDTO.DenyRequestsDTO;
import com.app.domain.model.ResponseDTO.SuccessResponse;
import com.app.domain.model.Users;
import com.app.domain.model.Utilities.DashboardRequestsAndPending;
import com.app.domain.model.Utilities.MySubscriptions;
import com.app.domain.model.Utilities.Pending;
import com.app.domain.model.Utilities.SubscriptionType;
import com.app.domain.model.Wishlist.StatusSubscribers;
import com.app.domain.model.Wishlist.Visibility;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.model.Wishlist.WishlistSubscribers;
import com.app.domain.repository.User.UserRepository;
import com.app.domain.repository.Wishlist.*;
import com.app.service.ApproveDenyInviteService;
import com.app.utils.AuthenticationUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApproveDenyInviteServiceImpl implements ApproveDenyInviteService {

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    PendingInvitationsRepository pendingInvitationsRepository;

    @Autowired
    PendingRepository pendingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WishlistSubscribersRepository wishlistSubscribersRepository;

    @Autowired
    DashboardRequestsAndPendingRepository dashboardRequestsAndPendingRepository;

    @Autowired
    MySubscriptionsRepository mySubscriptionsRepository;

    @Autowired
    DashboardRequestsSubscribersRepository dashboardRequestsSubscribersRepository;

    public Wishlist findWishlist(Integer idWishlist) {
        return wishlistRepository.findById(idWishlist)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));

    }

    public Users findUser(Integer idOwnerInvite) {
        return userRepository.findById(idOwnerInvite)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

    }


    private void updateCountsTotalWishlists(Wishlist existWishlist) {

        int totalSubscribers = wishlistSubscribersRepository.countByWishlist(existWishlist.getId_wishlist());

        List<MySubscriptions> subscriptions = mySubscriptionsRepository.findById_wishlist(existWishlist.getId_wishlist());

        for (MySubscriptions subscription : subscriptions) {
            subscription.setCount_subscribers_wishlist(totalSubscribers);
        }

        mySubscriptionsRepository.saveAll(subscriptions);
    }

    @Override
    @Transactional
    public ResponseEntity<?> approveInvite(ApproveSubscriberDTO approveSubscriberDTO) {

        Users user = AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> approveSubscriberDTO.approval_user_id());

        Wishlist wishlistExist = findWishlist(approveSubscriberDTO.id_wishlist_approve());

        DashboardRequestsAndPending selectedDashboard = user.getDashboardRequestsAndPending();

        List<Pending> requestsExist = selectedDashboard.getPending();

        Pending inviteSelected = requestsExist.stream()
                .filter(req -> req.getId_owner_user().equals(approveSubscriberDTO.id_user_requester()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("ID Owner/Request not found!"));

        updateSubscriptionsEntityUser(wishlistExist, inviteSelected);
        updateCountsTotalWishlists(wishlistExist);

        return ResponseEntity.ok(new SuccessResponse("The request was approved successfully."));
    }


    @Override
    @Transactional
    public void denyInvite(DenyRequestsDTO denyRequests) {

        Users authenticated = AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> denyRequests.id_user_responsible_deny());

        Wishlist wishlistExist = findWishlist(denyRequests.id_wishlist());

        DashboardRequestsAndPending dashboardUser = authenticated.getDashboardRequestsAndPending();

        List<Pending> pendingExist = dashboardUser.getPending();

        Pending inviteSelected = pendingExist.stream()
                .filter(req -> req.getId_owner_user().equals(denyRequests.id_requester()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("ID not found!"));

        removeMySubscribersMyPending(wishlistExist, inviteSelected);
        removeMySubscribersInvitationPending(wishlistExist, inviteSelected);
    }

    @Override
    @Transactional
    public void cancelInvite(CancelRequestDTO cancelRequests) {

        AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> cancelRequests.id_responsible_cancel());

        Wishlist wishlistExist = findWishlist(cancelRequests.id_wishlist());

        DashboardRequestsSubscribers dashboardRequests = wishlistExist.getDashboardRequestsSubscribers();

        List<PendingInvitations> pendingsExist = dashboardRequests.getPendingInvitations();

        PendingInvitations inviteSelected = pendingsExist.stream()
                .filter(req -> req.getId_user_guest().equals(cancelRequests.id_owner_approve()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("ID not found!"));

        removeSubscribersPendingWishlist(wishlistExist, inviteSelected);
        removeSubscribersPending(wishlistExist, inviteSelected);
    }

    private DashboardRequestsAndPending informationUser(PendingInvitations selectedUser) {
        Integer idSelectedUser = selectedUser.getId_user_guest();

        Users userExist = userRepository.findById(idSelectedUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userExist.getDashboardRequestsAndPending();
    }

    @Transactional
    private void removeSubscribersPending(Wishlist wishlistExist, PendingInvitations inviteSelected) {

        DashboardRequestsAndPending selectedDashboardUserApprove = informationUser(inviteSelected);

        List<Pending> existPendings = selectedDashboardUserApprove.getPending();

        for(Pending pending : existPendings) {
            if(wishlistExist.getId_wishlist().equals(pending.getId_wishlist())) {
                if(inviteSelected.getId_user_guest().equals(pending.getId_user_guest())) {
                    Integer idPending = pending.getId_pending();
                    pendingRepository.deleteByIdPending(idPending);
                    break;
                }
            }
        }

        selectedDashboardUserApprove.setCount_pending(selectedDashboardUserApprove.getCount_pending() - 1);
        dashboardRequestsAndPendingRepository.save(selectedDashboardUserApprove);

    }

    @Transactional
    private void removeSubscribersPendingWishlist(Wishlist wishlistExist, PendingInvitations inviteSelected) {

        DashboardRequestsSubscribers selectedDashboard = wishlistExist.getDashboardRequestsSubscribers();

        List<PendingInvitations> existListInvitations = selectedDashboard.getPendingInvitations();

        for(PendingInvitations invitations : existListInvitations) {
            if(wishlistExist.getId_wishlist().equals(invitations.getId_wishlist())) {
                if(inviteSelected.getId_user_guest().equals(invitations.getId_user_guest())) {
                    Integer idInvite = invitations.getId_pending_invitation();
                    pendingInvitationsRepository.deleteByIdInvitation(idInvite);
                    break;
                }
            }
        }

        selectedDashboard.setCount_pending_invitations(selectedDashboard.getCount_pending_invitations() - 1);
        dashboardRequestsSubscribersRepository.save(selectedDashboard);

    }



    private DashboardRequestsAndPending informationUserApprove(Pending selectedUser) {
        Integer idSelectedUser = selectedUser.getId_user_guest();

        Users userExist = userRepository.findById(idSelectedUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userExist.getDashboardRequestsAndPending();
    }

    @Transactional
    private void updateSubscriptionsEntityUser(Wishlist wishlistExist, Pending inviteSelected) {

        DashboardRequestsAndPending dashboardUser = informationUserApprove(inviteSelected);

        MySubscriptions newSubscription = new MySubscriptions();

        newSubscription.setId_user(inviteSelected.getId_user_guest());
        newSubscription.setUsername(inviteSelected.getUsername_guest());
        newSubscription.setDate_you_joined(new Date());
        newSubscription.setUserWithConnectionOwner(inviteSelected.isUserWithConnection());
        newSubscription.setUri_img_wishlist(wishlistExist.getUrl_img());
        newSubscription.setId_wishlist(wishlistExist.getId_wishlist());
        newSubscription.setName_wishlist(wishlistExist.getWishlist_name());
        newSubscription.setIdentity_wishlist(wishlistExist.getWishlist_identity());
        newSubscription.setVisibility(Visibility.valueOf(wishlistExist.getVisibility()));
        newSubscription.setCategory(wishlistExist.getCategory());
        newSubscription.setHasProductsByRecommendationPending(false);
        newSubscription.setCount_products_by_recommendation_total(0);
        newSubscription.setCount_products_by_recommendation_pending(0);
        newSubscription.setCount_likes(0);
        newSubscription.setCreation_date_wishlist(wishlistExist.getCreation_date());
        newSubscription.setStatusSubscribers(StatusSubscribers.valueOf(StatusSubscribers.SUBSCRIBED.getStatus_wishlist_subscribers()));
        newSubscription.setSubscriptionType(SubscriptionType.valueOf("GUEST"));
        newSubscription.setDashboard_requests_pending(dashboardUser);

        mySubscriptionsRepository.save(newSubscription);

        dashboardUser.addSubscriber(newSubscription);
        dashboardUser.setCount_subscriptions(dashboardUser.getMySubscriptions().size());
        dashboardRequestsAndPendingRepository.save(dashboardUser);

        updateSubscriptionsWishlist(wishlistExist, inviteSelected);
        removeMySubscribersMyPending(wishlistExist, inviteSelected);
    }


    @Transactional
    private void removeMySubscribersMyPending(Wishlist wishlistExist, Pending inviteSelected) {

        DashboardRequestsAndPending selectedDashboardUserApprove = informationUserApprove(inviteSelected);

        List<Pending> existPendings = selectedDashboardUserApprove.getPending();

        for(Pending pending : existPendings) {
            if(wishlistExist.getId_wishlist().equals(pending.getId_wishlist())) {
                if(inviteSelected.getId_user_guest().equals(pending.getId_user_guest())) {
                    Integer idPending = pending.getId_pending();
                    pendingRepository.deleteByIdPending(idPending);
                    break;
                }
            }
        }

        selectedDashboardUserApprove.setCount_pending(selectedDashboardUserApprove.getCount_pending() - 1);
        dashboardRequestsAndPendingRepository.save(selectedDashboardUserApprove);

    }

    @Transactional
    private void updateSubscriptionsWishlist(Wishlist wishlistExist, Pending inviteSelected) {


        WishlistSubscribers newSubscriber = new WishlistSubscribers();

        newSubscriber.setId_user(inviteSelected.getId_user_guest());
        newSubscriber.setUsername(inviteSelected.getUsername_guest());
        newSubscriber.setWishlist(wishlistExist);
        newSubscriber.setDate_you_joined(new Date());
        newSubscriber.setUserWithConnection(inviteSelected.isUserWithConnection());
        newSubscriber.setStatusSubscribers(StatusSubscribers.valueOf("SUBSCRIBEDBYINVITATION"));

        wishlistSubscribersRepository.save(newSubscriber);

        wishlistExist.addSubscribers(newSubscriber);
        wishlistExist.setCount_total_subscribers(wishlistExist.getWishlistSubscribers().size());

        wishlistRepository.save(wishlistExist);

        removeMySubscribersInvitationPending(wishlistExist, inviteSelected);
    }

    @Transactional
    private void removeMySubscribersInvitationPending(Wishlist wishlistExist, Pending inviteSelected) {

        DashboardRequestsSubscribers selectedDashboard = wishlistExist.getDashboardRequestsSubscribers();

        List<PendingInvitations> existListInvitations = selectedDashboard.getPendingInvitations();

        for(PendingInvitations invitations : existListInvitations) {
            if(wishlistExist.getId_wishlist().equals(invitations.getId_wishlist())) {
                if(inviteSelected.getId_user_guest().equals(invitations.getId_user_guest())) {
                    Integer idInvite = invitations.getId_pending_invitation();
                    pendingInvitationsRepository.deleteByIdInvitation(idInvite);
                    break;
                }
            }
        }

        selectedDashboard.setCount_pending_invitations(selectedDashboard.getCount_pending_invitations() - 1);
        dashboardRequestsSubscribersRepository.save(selectedDashboard);

    }




}
