package com.app.service;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import com.app.domain.model.DashboardWishlists;
import com.app.domain.model.ResponseDTO.UpdateWishlistDTO;
import com.app.domain.model.ResponseDTO.WishlistDTO;
import com.app.domain.model.Wishlist.EventsInWishlists;
import com.app.domain.model.Wishlist.Tags;
import com.app.domain.model.Wishlist.Wishlist;
import org.springframework.stereotype.Service;

@Service
public interface WishlistService {

    WishlistDTO createWishlist(Wishlist newWishlist, WishlistDTO wishlistDTO, Tags newTags, DashboardWishlists dashboardWishlists,
                               EventsInWishlists newEventInWishlist, DashboardRequestsSubscribers dashboardRequestsSubscribers, DashboardProducts dashboardProducts);

    UpdateWishlistDTO updateWishlist( Wishlist wishlistUpdated, Integer id_wishlist, UpdateWishlistDTO updateWishlistDTO);

    Wishlist findById(Integer id_wishlist);
}
