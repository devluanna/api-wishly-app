package com.app.events.wishlist;

import com.app.domain.model.Wishlist.Wishlist;
import org.springframework.context.ApplicationEvent;

public class WishlistUpdatedEvent extends ApplicationEvent {
    private final Wishlist wishlist;

    public WishlistUpdatedEvent(Object source, Wishlist wishlist) {
        super(source);
        this.wishlist = wishlist;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }
}