package com.app.events.users;

import com.app.domain.model.Users;
import com.app.domain.repository.Wishlist.EventsRepository;
import com.app.domain.repository.Wishlist.MySubscriberRequestsRepository;
import com.app.domain.repository.Wishlist.PendingRepository;
import com.app.domain.repository.Wishlist.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserOwnerUpdateListener {
    @Autowired
    PendingRepository pendingRepository;
    @Autowired
    MySubscriberRequestsRepository mySubscriberRequestsRepository;
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    WishlistRepository wishlistRepository;

   // @Autowired
   //NotificationUserRepository notificationUserRepository; PENSAR POR ULTIMO FAZER DEPOIS QUE IMPLEMENTAR TODAS AS NOTIFICATIONS


    //WISHLISTS REQUESTS SUBSCRIBERS NAO REFLETIU!
    //PENDING INVITATION

    @EventListener
    @Transactional
    public void handleUserResponsibleUpdatedEvent(UserUpdatedEvent event) {
        Users updatedUser = event.getUsers();
        String newUserName = updatedUser.getUsername();
        Integer userIdOwner = updatedUser.getId_user();

        pendingRepository.updateUsernameOwner(userIdOwner, newUserName);
        mySubscriberRequestsRepository.updateUsernameOwner(userIdOwner, newUserName);
        eventsRepository.updateUsernameOwner(userIdOwner, newUserName);
        wishlistRepository.updateUsernameOwner(userIdOwner, newUserName);


    }
}
