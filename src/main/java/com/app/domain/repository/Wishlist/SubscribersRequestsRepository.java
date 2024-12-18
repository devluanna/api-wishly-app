package com.app.domain.repository.Wishlist;

import com.app.domain.model.DashboardWishlist.SubscriberRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SubscribersRequestsRepository extends JpaRepository<SubscriberRequests, Integer> {

    @Modifying
    @Query("DELETE FROM SubscriberRequests r WHERE r.id_subscriber_request = :id_subscriber_request")
    void deleteByIdRequests(@Param("id_subscriber_request") Integer id_subscriber_request);

    @Modifying
    @Query("UPDATE SubscriberRequests d SET d.name_wishlist = :name_wishlist WHERE d.id_wishlist = :wishlistId")
    void updateWishlistName(@Param("wishlistId") Integer wishlistId, @Param("name_wishlist") String name_wishlist);


}
