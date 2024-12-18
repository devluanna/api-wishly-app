package com.app.service;

import com.app.domain.model.ResponseDTO.SubscriberRequestDTO;
import com.app.domain.model.Wishlist.WishlistSubscribers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface WishlistSubscriberService {
    ResponseEntity<?> signupInWishlist(SubscriberRequestDTO subscriberRequestDTO, WishlistSubscribers wishlistSubscribers);
}
