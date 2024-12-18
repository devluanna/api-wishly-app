package com.app.domain.repository.Wishlist;

import com.app.domain.model.Wishlist.WishlistsInEvents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistsInEventsRepository extends JpaRepository<WishlistsInEvents, Integer> {
}
