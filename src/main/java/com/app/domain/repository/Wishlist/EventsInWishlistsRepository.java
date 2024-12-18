package com.app.domain.repository.Wishlist;

import com.app.domain.model.Wishlist.EventsInWishlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsInWishlistsRepository extends JpaRepository<EventsInWishlists, Integer> {
}
