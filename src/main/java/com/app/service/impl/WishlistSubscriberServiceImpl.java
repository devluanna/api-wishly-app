package com.app.service.impl;
import com.app.domain.model.Connections;
import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import com.app.domain.model.DashboardWishlist.SubscriberRequests;
import com.app.domain.model.ResponseDTO.SubscriberRequestDTO;
import com.app.domain.model.ResponseDTO.SuccessResponse;
import com.app.domain.model.Users;
import com.app.domain.model.Utilities.*;
import com.app.domain.model.Wishlist.*;
import com.app.domain.repository.User.UserRepository;
import com.app.domain.repository.Wishlist.*;
import com.app.exception.BusinessRuleException;
import com.app.service.WishlistSubscriberService;
import com.app.utils.AuthenticationUtils;
import com.app.utils.VisibilityHandler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WishlistSubscriberServiceImpl implements WishlistSubscriberService {

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    WishlistSubscribersRepository wishlistSubscribersRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VisibilityHandler visibilityHandler;

    @Autowired
    DashboardRequestsSubscribersRepository dashboardRequestsRepository;

    @Autowired
    SubscribersRequestsRepository requestsRepository;

    @Autowired
    MySubscriptionsRepository  mySubscriptionsRepository;

    @Autowired
    DashboardRequestsAndPendingRepository dashboardRequestsUser;

    @Autowired
    MySubscriberRequestsRepository myRequestsRepository;

    @Override
    public ResponseEntity<?> signupInWishlist(SubscriberRequestDTO subscriberRequestDTO, WishlistSubscribers wishlistSubscribers) {

        Users authenticatedUser = AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> subscriberRequestDTO.id_user());

        Wishlist existWishlist = findWishlist(subscriberRequestDTO.id_wishlist_add());

        if (visibilityHandler.canSubscribe(authenticatedUser, existWishlist)) {
            subscribeUserToWishlist(existWishlist, authenticatedUser, subscriberRequestDTO);
            return ResponseEntity.ok(new SuccessResponse("Successfully subscribed to the public wishlist."));
        } else if (visibilityHandler.requiresRequest(existWishlist)) {
            requestSubscriberInWishlist(existWishlist, authenticatedUser, subscriberRequestDTO);
            return ResponseEntity.ok(new SuccessResponse("Your request has been sent successfully for the private wishlist."));
        }

        throw new BusinessRuleException("Not allowed to subscribe to this wishlist", HttpStatus.FORBIDDEN);
    }

    private Wishlist findWishlist(Integer idWishlist) {
        return wishlistRepository.findById(idWishlist)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
    }

    private void validateSubscriberRequestExist(Wishlist existWishlist, Users authenticatedUser) {
        validateRequestExist(existWishlist, authenticatedUser);
        validatePendingExist(existWishlist, authenticatedUser);

    }

    private void validatePendingExist(Wishlist existWishlist, Users authenticatedUser) {

        DashboardRequestsAndPending dashboardUser = authenticatedUser.getDashboardRequestsAndPending();

        List<Pending> pendingExist = dashboardUser.getPending();

        for(Pending pend : pendingExist) {
            if(pend.getId_wishlist().equals(existWishlist.getId_wishlist())) {
                if (pend.getId_user_guest().equals(authenticatedUser.getId_user())) {
                    throw new BusinessRuleException("It is not possible to register," +
                            "you already have an open invitation pending your approval.", HttpStatus.BAD_REQUEST);
                }
            }
        }

    }

    private void validateRequestExist(Wishlist existWishlist, Users authenticatedUser) {

        List<SubscriberRequests> requestsExist = existWishlist.getDashboardRequestsSubscribers().getSubscriberRequests();

        for(SubscriberRequests req : requestsExist) {
            if(req.getId_user().equals(authenticatedUser.getId_user())) {
                throw new BusinessRuleException("You have already subscribed to this wishlist", HttpStatus.BAD_REQUEST);
            }
        }

    }


    private void validateSubscriberExist(Wishlist existWishlist, Users authenticatedUser) {

        List<WishlistSubscribers> subscriberExist = existWishlist.getWishlistSubscribers();

        for(WishlistSubscribers req : subscriberExist) {
            if(req.getId_user().equals(authenticatedUser.getId_user())) {
                throw new BusinessRuleException("You have already subscribed to this wishlist", HttpStatus.BAD_REQUEST);
            }
        }

    }


    private void subscribeUserToWishlist(Wishlist existWishlist, Users authenticatedUser, SubscriberRequestDTO subscriberRequestDTO) {

        validateUserSubscriber(existWishlist, subscriberRequestDTO);
        validateSubscriberExist(existWishlist, authenticatedUser);

        WishlistSubscribers newSubscriber = new WishlistSubscribers();

        validateConnections(existWishlist, subscriberRequestDTO, newSubscriber);
        newSubscriber.setId_user(authenticatedUser.getId_user());
        newSubscriber.setUsername(authenticatedUser.getUsername());
        newSubscriber.setWishlist(existWishlist);
        newSubscriber.setDate_you_joined(new Date());
        newSubscriber.setStatusSubscribers(StatusSubscribers.valueOf("SUBSCRIBED"));
        wishlistSubscribersRepository.save(newSubscriber);

        existWishlist.addSubscribers(newSubscriber);
        existWishlist.setCount_total_subscribers(existWishlist.getWishlistSubscribers().size());

        wishlistRepository.save(existWishlist);

        addNewSubscriberDashboardUser(authenticatedUser,existWishlist,newSubscriber);
    }

    public void addNewSubscriberDashboardUser(Users authenticatedUser, Wishlist existWishlist, WishlistSubscribers newSubscriber) {

        DashboardRequestsAndPending selectedDashboardSubsUser = authenticatedUser.getDashboardRequestsAndPending();

        MySubscriptions newSubscription = new MySubscriptions();
        newSubscription.setId_user(authenticatedUser.getId_user());
        newSubscription.setUsername(authenticatedUser.getUsername());
        newSubscription.setDate_you_joined(newSubscriber.getDate_you_joined());
        newSubscription.setUserWithConnectionOwner(newSubscriber.isUserWithConnection());
        newSubscription.setUri_img_wishlist(existWishlist.getUrl_img());
        newSubscription.setId_wishlist(existWishlist.getId_wishlist());
        newSubscription.setName_wishlist(existWishlist.getWishlist_name());
        newSubscription.setIdentity_wishlist(existWishlist.getWishlist_identity());
        newSubscription.setVisibility(Visibility.valueOf(existWishlist.getVisibility()));
        newSubscription.setCategory(existWishlist.getCategory());
        newSubscription.setHasProductsByRecommendationPending(false);
        newSubscription.setCount_products_by_recommendation_total(0);
        newSubscription.setCount_products_by_recommendation_pending(0);
        newSubscription.setCount_likes(0);
        newSubscription.setCreation_date_wishlist(existWishlist.getCreation_date());
        newSubscription.setStatusSubscribers(StatusSubscribers.valueOf("SUBSCRIBED"));
        newSubscription.setSubscriptionType(SubscriptionType.valueOf("SUBSCRIBED"));
        newSubscription.setDashboard_requests_pending(selectedDashboardSubsUser);
        newSubscription.setCount_subscribers_wishlist(newSubscription.getCount_subscribers_wishlist() + 1);

        mySubscriptionsRepository.save(newSubscription);

        selectedDashboardSubsUser.addSubscriber(newSubscription);
        selectedDashboardSubsUser.setCount_subscriptions(selectedDashboardSubsUser.getMySubscriptions().size());
        dashboardRequestsUser.save(selectedDashboardSubsUser);

        updateCountsTotalWishlist(existWishlist);

    }

    private void updateCountsTotalWishlist(Wishlist existWishlist) {

        int totalSubscribers = wishlistSubscribersRepository.countByWishlist(existWishlist.getId_wishlist());

        List<MySubscriptions> subscriptions = mySubscriptionsRepository.findById_wishlist(existWishlist.getId_wishlist());

        for (MySubscriptions subscription : subscriptions) {
            subscription.setCount_subscribers_wishlist(totalSubscribers);
        }

        mySubscriptionsRepository.saveAll(subscriptions);


    }


    private void requestSubscriberInWishlist(Wishlist existWishlist, Users authenticatedUser, SubscriberRequestDTO subscriberRequestDTO) {

        DashboardRequestsSubscribers selectedDashboardRequests = existWishlist.getDashboardRequestsSubscribers();
        validateSubscriberRequestExist(existWishlist,authenticatedUser);

        Integer idWishlist = existWishlist.getId_wishlist();

        SubscriberRequests newRequest = new SubscriberRequests();
        newRequest.setId_user(authenticatedUser.getId_user());
        newRequest.setUsername(authenticatedUser.getUsername());
        newRequest.setDate_user_requested(new Date());
        newRequest.setName_wishlist(existWishlist.getWishlist_name());
        newRequest.setStatusSubscribers(StatusSubscribers.valueOf("PENDINGOWNERAPPROVAL"));
        newRequest.setId_wishlist(idWishlist);
        newRequest.setDashboard_requests_subscribers(selectedDashboardRequests);

        requestsRepository.save(newRequest);

        selectedDashboardRequests.addSubscriberRequests(newRequest);
        selectedDashboardRequests.setCount_subscriber_requests(selectedDashboardRequests.getSubscriberRequests().size());

        dashboardRequestsRepository.save(selectedDashboardRequests);

        addRequestsDashboardUser(existWishlist, authenticatedUser, newRequest);

    }

    private void addRequestsDashboardUser(Wishlist existWishlist, Users authenticatedUser,SubscriberRequests newRequest) {

        DashboardRequestsAndPending selectedDashboardSubsUser = authenticatedUser.getDashboardRequestsAndPending();

        Requests newRequestUser = new Requests();
        newRequestUser.setId_user(authenticatedUser.getId_user());
        newRequestUser.setUsername(authenticatedUser.getUsername());
        newRequestUser.setId_owner_user(existWishlist.getId_owner());
        newRequestUser.setUsername_owner(existWishlist.getUsername_owner());
        newRequestUser.setId_wishlist(existWishlist.getId_wishlist());
        newRequestUser.setWishlist_name(existWishlist.getWishlist_name());
        newRequestUser.setDate_of_request(newRequest.getDate_user_requested());
        newRequestUser.setStatusSubscribers("PENDINGOWNERAPPROVAL");
        newRequestUser.setUserWithConnection(newRequest.isUserWithConnection());
        newRequestUser.setDashboard_requests_pending(selectedDashboardSubsUser);

        selectedDashboardSubsUser.addRequests(newRequestUser);
        selectedDashboardSubsUser.setCount_requests(selectedDashboardSubsUser.getRequests().size());

        myRequestsRepository.save(newRequestUser);
        dashboardRequestsUser.save(selectedDashboardSubsUser);

    }

    private void validateUserSubscriber(Wishlist existWishlist, SubscriberRequestDTO subscriberRequestDTO) {
        if(existWishlist.getId_owner().equals(subscriberRequestDTO.id_user())) {
            throw new BusinessRuleException("User who owns the wishlist cannot subscribe.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateConnections(Wishlist existWishlist, SubscriberRequestDTO subscriberRequestDTO, WishlistSubscribers newSubscriber) {
        Integer idOwner = existWishlist.getId_owner();

        Users existUserOwner = userRepository.findById(idOwner).orElseThrow(() -> new EntityNotFoundException("User not Found!"));

        if(existUserOwner != null) {
            List<Connections> listConnections = existUserOwner.getConnectionsDashboard().getConnections();

            for(Connections conn : listConnections ) {
                if(subscriberRequestDTO.id_user().equals(conn.getId_user_connection())) {
                    newSubscriber.setUserWithConnection(true);
                }
            }
        } else {
            throw new BusinessRuleException("User not found", HttpStatus.BAD_REQUEST);
        }
    }






}
