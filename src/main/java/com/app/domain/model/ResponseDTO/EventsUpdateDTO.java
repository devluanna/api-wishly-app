package com.app.domain.model.ResponseDTO;

import com.app.domain.model.Wishlist.StatusWishlistEvent;

import java.util.Date;

public record EventsUpdateDTO(String event_name, String event_description, String category, String status, Date start_date, Date end_date, Date date_last_update) {

}
