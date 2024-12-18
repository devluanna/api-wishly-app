package com.app.domain.repository.Wishlist;

import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.model.Wishlist.WishlistSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistSubscribersRepository extends JpaRepository<WishlistSubscribers, Integer> {

    @Query("SELECT COUNT(ws) FROM WishlistSubscribers ws WHERE ws.wishlist.id_wishlist = :id_wishlist")
    int countByWishlist(@Param("id_wishlist") Integer id_wishlist);



}
