package com.app.domain.model.ResponseDTO;

import com.app.domain.model.Wishlist.Tags;

import java.util.Date;
import java.util.List;


public record UpdateWishlistDTO(
        String wishlist_name, String url_img, String description, String visibility, Boolean isRequiredRequest, String category, String sub_category,
        List<Tags> tags, String status_wishlist, Date last_update_date, Boolean enablesProductReservations, Boolean enableProductsByRecommendation,
        Boolean useEventDate

) {
}
