package com.app.service.impl;

import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import com.app.domain.model.DashboardWishlist.PendingInvitations;
import com.app.domain.model.DashboardWishlist.SubscriberRequests;
import com.app.domain.model.ResponseDTO.ApproveSubscriberDTO;
import com.app.domain.model.ResponseDTO.CancelRequestDTO;
import com.app.domain.model.ResponseDTO.DenyRequestsDTO;
import com.app.domain.model.ResponseDTO.SuccessResponse;
import com.app.domain.model.Users;
import com.app.domain.model.Utilities.*;
import com.app.domain.model.Wishlist.StatusSubscribers;
import com.app.domain.model.Wishlist.Visibility;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.model.Wishlist.WishlistSubscribers;
import com.app.domain.repository.User.UserRepository;
import com.app.domain.repository.Wishlist.*;
import com.app.exception.BusinessRuleException;
import com.app.service.ApproveDenySubService;
import com.app.utils.AuthenticationUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;



@Service
public class ApproveDenySubServiceImpl implements ApproveDenySubService {

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    SubscribersRequestsRepository subscribersRequestsRepository;

    @Autowired
    DashboardRequestsSubscribersRepository dashboardRequestsSubscribers;

    @Autowired
    DashboardRequestsAndPendingRepository dashboardRequestsAndPendingRepository;

    @Autowired
    MySubscriberRequestsRepository mySubscriberRequestsRepository;

    @Autowired
    WishlistSubscribersRepository wishlistSubscribersRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MySubscriptionsRepository mySubscriptionsRepository;


    private Wishlist findWishlist(Integer idWishlist) {
        return wishlistRepository.findById(idWishlist)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> approveSubscriber(ApproveSubscriberDTO approveSubscriberDTO) {

        AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> approveSubscriberDTO.approval_user_id());

        Wishlist wishlistExist = findWishlist(approveSubscriberDTO.id_wishlist_approve());

        DashboardRequestsSubscribers dashboardSelected = wishlistExist.getDashboardRequestsSubscribers();

        List<SubscriberRequests> existSubscribers = dashboardSelected.getSubscriberRequests();

        SubscriberRequests selectedUser = existSubscribers.stream()
                .filter(req -> req.getId_user().equals(approveSubscriberDTO.id_user_requester()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Request not found."));

        subscribersRequestsRepository.deleteByIdRequests(selectedUser.getId_subscriber_request());

        dashboardSelected.setCount_subscriber_requests(dashboardSelected.getCount_subscriber_requests() - 1);
        dashboardRequestsSubscribers.save(dashboardSelected);

        updateSubscriptionsWishlist(wishlistExist, selectedUser);
        updateCountsTotalWishlistsPrivate(wishlistExist);

        return ResponseEntity.ok(new SuccessResponse("The request was approved successfully."));
    }

    @Override
    @Transactional
    public void denyInvite(DenyRequestsDTO denyRequests) {
        AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> denyRequests.id_user_responsible_deny());

        Wishlist wishlistExist = findWishlist(denyRequests.id_wishlist());

        DashboardRequestsSubscribers dashboardSelected = wishlistExist.getDashboardRequestsSubscribers();

        List<SubscriberRequests> existSubscribers = dashboardSelected.getSubscriberRequests();

        SubscriberRequests selectedUser = existSubscribers.stream()
                .filter(req -> req.getId_user().equals(denyRequests.id_requester()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Request not found."));

        removeMySubscribersPending(selectedUser, wishlistExist);
        removeMySubscribersRequest(wishlistExist, selectedUser);
    }

    @Override
    @Transactional
    public void cancelRequests(CancelRequestDTO cancelRequests) {

        Users authenticated = AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> cancelRequests.id_responsible_cancel());

        Wishlist wishlistExist = findWishlist(cancelRequests.id_wishlist());

        DashboardRequestsAndPending dashboardRequests = authenticated.getDashboardRequestsAndPending();

        List<Requests> existRequests = dashboardRequests.getRequests();

        Requests requestSelected = existRequests.stream()
                .filter(req -> req.getId_user().equals(cancelRequests.id_responsible_cancel()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("ID not found!"));

        removeMyRequests(wishlistExist, requestSelected, authenticated);
        removeRequestsWishlist(wishlistExist);
    }

    @Transactional
    private void removeRequestsWishlist(Wishlist wishlistExist) {

        DashboardRequestsSubscribers dashboardWishlist = wishlistExist.getDashboardRequestsSubscribers();

        List<SubscriberRequests> existRequest = dashboardWishlist.getSubscriberRequests();

        for (SubscriberRequests req : existRequest) {
            if (req.getId_wishlist().equals(wishlistExist.getId_wishlist())) {
                Integer idRequest = req.getId_subscriber_request();
                mySubscriberRequestsRepository.deleteByIdRequests(idRequest);
                break;
            }
        }

        dashboardWishlist.setCount_subscriber_requests(dashboardWishlist.getCount_subscriber_requests() - 1);
        dashboardRequestsSubscribers.save(dashboardWishlist);

    }
    @Transactional
    private void removeMyRequests(Wishlist wishlistExist, Requests requestSelected, Users authenticated) {

        DashboardRequestsAndPending selectedDashboard = authenticated.getDashboardRequestsAndPending();

        List<Requests> existRequests = selectedDashboard.getRequests();

        for(Requests sub : existRequests) {
            if(wishlistExist.getId_wishlist().equals(sub.getId_wishlist())) {
                if(requestSelected.getId_user().equals(sub.getId_user())) {
                    Integer idSubscriberRequest = sub.getId_request();
                    subscribersRequestsRepository.deleteByIdRequests(idSubscriberRequest);
                    break;
                }
            }
        }

        selectedDashboard.setCount_requests(selectedDashboard.getCount_requests() - 1);
        dashboardRequestsAndPendingRepository.save(selectedDashboard);

    }

    @Transactional
    private void removeMySubscribersRequest(Wishlist wishlistExist, SubscriberRequests selectedUser) {

        DashboardRequestsSubscribers selectedDashboard = wishlistExist.getDashboardRequestsSubscribers();

        List<SubscriberRequests> existRequests = selectedDashboard.getSubscriberRequests();

        for(SubscriberRequests sub : existRequests) {
            if(wishlistExist.getId_wishlist().equals(sub.getId_wishlist())) {
                if(selectedUser.getId_user().equals(sub.getId_user())) {
                    Integer idSubscriberRequest = sub.getId_subscriber_request();
                    subscribersRequestsRepository.deleteByIdRequests(idSubscriberRequest);
                    break;
                }
            }
        }

        selectedDashboard.setCount_subscriber_requests(selectedDashboard.getCount_subscriber_requests() - 1);
        dashboardRequestsSubscribers.save(selectedDashboard);

    }


    @Transactional
    private void updateSubscriptionsWishlist(Wishlist wishlistExist, SubscriberRequests selectedUser) {

        WishlistSubscribers newSubscriber = new WishlistSubscribers();

        newSubscriber.setId_user(selectedUser.getId_user());
        newSubscriber.setUsername(selectedUser.getUsername());
        newSubscriber.setWishlist(wishlistExist);
        newSubscriber.setDate_you_joined(new Date());
        newSubscriber.setUserWithConnection(selectedUser.isUserWithConnection());
        newSubscriber.setStatusSubscribers(StatusSubscribers.valueOf("SUBSCRIBED"));

        wishlistSubscribersRepository.save(newSubscriber);

        wishlistExist.addSubscribers(newSubscriber);
        wishlistExist.setCount_total_subscribers(wishlistExist.getWishlistSubscribers().size());

        wishlistRepository.save(wishlistExist);

        updateSubscriptionsEntityUser(wishlistExist, selectedUser);
        removeMySubscribersPending(selectedUser, wishlistExist);

    }

    private void updateCountsTotalWishlistsPrivate(Wishlist existWishlist) {

        int totalSubscribers = wishlistSubscribersRepository.countByWishlist(existWishlist.getId_wishlist());

        List<MySubscriptions> subscriptions = mySubscriptionsRepository.findById_wishlist(existWishlist.getId_wishlist());

        for (MySubscriptions subscription : subscriptions) {
            subscription.setCount_subscribers_wishlist(totalSubscribers);
        }

        mySubscriptionsRepository.saveAll(subscriptions);
    }



    private DashboardRequestsAndPending informationUserRequester(SubscriberRequests selectedUser) {
        Integer idSelectedUser = selectedUser.getId_user();

        Users userExist = userRepository.findById(idSelectedUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userExist.getDashboardRequestsAndPending();
    }

    @Transactional
    private void removeMySubscribersPending(SubscriberRequests selectedUser, Wishlist wishlistExist) {

        DashboardRequestsAndPending dashboardUser = informationUserRequester(selectedUser);

        List<Requests> existRequests = dashboardUser.getRequests();

        for (Requests req : existRequests) {
            if (req.getId_wishlist().equals(wishlistExist.getId_wishlist())) {
                Integer idRequest = req.getId_request();
                mySubscriberRequestsRepository.deleteByIdRequests(idRequest);
                break;
            }
        }

        dashboardUser.setCount_requests(dashboardUser.getCount_requests() - 1);
        dashboardRequestsAndPendingRepository.save(dashboardUser);

    }

    @Transactional
    private void updateSubscriptionsEntityUser(Wishlist wishlistExist, SubscriberRequests selectedUser) {

        DashboardRequestsAndPending dashboardUser = informationUserRequester(selectedUser);

        MySubscriptions newSubscription = new MySubscriptions();

        newSubscription.setId_user(selectedUser.getId_user());
        newSubscription.setUsername(selectedUser.getUsername());
        newSubscription.setDate_you_joined(new Date());
        newSubscription.setUserWithConnectionOwner(selectedUser.isUserWithConnection());
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
        newSubscription.setStatusSubscribers(StatusSubscribers.valueOf("SUBSCRIBED"));
        newSubscription.setSubscriptionType(SubscriptionType.valueOf("SUBSCRIBED"));
        newSubscription.setDashboard_requests_pending(dashboardUser);

        mySubscriptionsRepository.save(newSubscription);

        dashboardUser.addSubscriber(newSubscription);
        dashboardUser.setCount_subscriptions(dashboardUser.getMySubscriptions().size());
        dashboardRequestsAndPendingRepository.save(dashboardUser);

    }


}
