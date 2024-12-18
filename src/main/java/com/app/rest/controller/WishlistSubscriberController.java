package com.app.rest.controller;

import com.app.domain.model.ResponseDTO.SubscriberRequestDTO;
import com.app.domain.model.Wishlist.WishlistSubscribers;
import com.app.service.WishlistSubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscriber")
public class WishlistSubscriberController {

    @Autowired
    WishlistSubscriberService wishlistSubscribersService;


    @PostMapping("/signup/wishlist")
    public ResponseEntity<?>  signupInWishlist(@RequestBody SubscriberRequestDTO subscriberRequestDTO, WishlistSubscribers wishlistSubscribers) {

        return wishlistSubscribersService.signupInWishlist(subscriberRequestDTO, wishlistSubscribers);

    }


}
