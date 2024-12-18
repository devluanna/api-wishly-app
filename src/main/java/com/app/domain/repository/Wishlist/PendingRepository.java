package com.app.domain.repository.Wishlist;

import com.app.domain.model.Utilities.Pending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingRepository extends JpaRepository<Pending, Integer> {

    @Modifying
    @Query("UPDATE Pending d SET d.wishlist_name = :wishlist_name WHERE d.id_wishlist = :wishlistId")
    void updateWishlistName(@Param("wishlistId") Integer wishlistId, @Param("wishlist_name") String wishlist_name);

    @Modifying
    @Query("UPDATE Pending d SET d.username_owner = :username_owner WHERE d.id_owner_user = :userIdOwner")
    void updateUsernameOwner(@Param("userIdOwner") Integer userIdOwner, @Param("username_owner") String username_owner);

    @Modifying
    @Query("DELETE FROM Pending r WHERE r.id_pending = :id_pending")
    void deleteByIdPending(@Param("id_pending") Integer id_pending);

}
