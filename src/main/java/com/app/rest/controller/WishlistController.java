package com.app.rest.controller;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import com.app.domain.model.DashboardWishlists;
import com.app.domain.model.ResponseDTO.UpdateWishlistDTO;
import com.app.domain.model.ResponseDTO.WishlistDTO;
import com.app.domain.model.Users;
import com.app.domain.model.Wishlist.EventsInWishlists;
import com.app.domain.model.Wishlist.Tags;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/wishlist")
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @PostMapping("/create")
    public ResponseEntity createWishlist(@RequestBody Wishlist newWishlist, WishlistDTO wishlistDTO, Tags newTags, DashboardWishlists dashboardWishlists,
                                         EventsInWishlists newEventInWishlist, DashboardRequestsSubscribers dashboardRequestsSubscribers, DashboardProducts dashboardProducts) {

        WishlistDTO wishlistCreated = wishlistService.createWishlist(newWishlist, wishlistDTO, newTags, dashboardWishlists, newEventInWishlist, dashboardRequestsSubscribers, dashboardProducts);

        return ResponseEntity.ok(wishlistCreated);

    }

    @PutMapping("/update/{id_wishlist}")
    public ResponseEntity updateWishlist(@PathVariable Integer id_wishlist, @RequestBody UpdateWishlistDTO updateWishlistDTO) {

        if (id_wishlist == null) {
            System.out.println("Wishlist not found!");
            return ResponseEntity.badRequest().build();
        }

        Wishlist wishlistUpdated = wishlistService.findById(id_wishlist);

        UpdateWishlistDTO wishlistSaved = wishlistService.updateWishlist(wishlistUpdated, id_wishlist, updateWishlistDTO);

        return ResponseEntity.ok(wishlistSaved);
    }

}

