package com.app.events.wishlist;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.repository.User.DashboardWishlistsRepository;
import com.app.domain.repository.Wishlist.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WishlistUpdateListener {

   @Autowired
   DashboardRequestsSubscribersRepository dashboardRequestsSubscribersRepository;
   @Autowired
    PendingRepository pendingUserRepository;
   @Autowired
    PendingInvitationsRepository pendingInvitationsRepository;
   @Autowired
    SubscribersRequestsRepository subscribersRequestsRepository;
   @Autowired
    MySubscriberRequestsRepository mySubscriberRequestsRepository;

    @EventListener
    @Transactional
    public void handleWishlistUpdatedEvent(WishlistUpdatedEvent event) {
        Wishlist updatedWishlist = event.getWishlist();
        String newWishlistName = updatedWishlist.getWishlist_name();
        Integer wishlistId = updatedWishlist.getId_wishlist();

        dashboardRequestsSubscribersRepository.updateWishlistName(wishlistId, newWishlistName);
        pendingUserRepository.updateWishlistName(wishlistId, newWishlistName);
        pendingInvitationsRepository.updateWishlistName(wishlistId, newWishlistName);
        subscribersRequestsRepository.updateWishlistName(wishlistId, newWishlistName);
        mySubscriberRequestsRepository.updateWishlistName(wishlistId, newWishlistName);

    }
}