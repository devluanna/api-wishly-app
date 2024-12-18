package com.app.domain.model.ResponseDTO;

import com.app.domain.model.Wishlist.EventsInWishlists;
import com.app.domain.model.Wishlist.Tags;


import java.util.Date;
import java.util.List;

public record WishlistDTO (  Integer wishlist_identity,
        String wishlist_name, String url_img, String description, Integer id_owner, String username_owner, String visibility, Boolean isRequiredRequest,
        String url_share, String category, String sub_category, List<Tags> tags, String status_wishlist,
        Date creation_date, Boolean enablesProductReservations, Boolean enableProductsByRecommendation, Boolean isACopiedWishlist,
                             Integer id_dashboard_wishlists, Boolean haveLinkedEvent, String name_event_linked,
                             EventsInWishlists eventsInWishlists, Integer id_dashboard_requests, Integer id_dashboard_products
) {
}
