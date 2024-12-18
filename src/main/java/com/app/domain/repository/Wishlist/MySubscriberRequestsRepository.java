package com.app.domain.repository.Wishlist;

import com.app.domain.model.DashboardWishlist.SubscriberRequests;
import com.app.domain.model.Utilities.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MySubscriberRequestsRepository extends JpaRepository<Requests, Integer> {

    @Modifying
    @Query("UPDATE Requests d SET d.wishlist_name = :wishlist_name WHERE d.id_wishlist = :wishlistId")
    void updateWishlistName(@Param("wishlistId") Integer wishlistId, @Param("wishlist_name") String wishlist_name);
    @Modifying
    @Query("DELETE FROM Requests r WHERE r.id_request = :id_request")
    void deleteByIdRequests(@Param("id_request") Integer id_request);

    @Modifying
    @Query("UPDATE Requests d SET d.username_owner = :username_owner WHERE d.id_owner_user = :userIdOwner")
    void updateUsernameOwner(@Param("userIdOwner") Integer userIdOwner, @Param("username_owner") String username_owner);

}
