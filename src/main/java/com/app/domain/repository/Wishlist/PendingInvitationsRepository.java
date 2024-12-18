package com.app.domain.repository.Wishlist;

import com.app.domain.model.DashboardWishlist.PendingInvitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingInvitationsRepository extends JpaRepository<PendingInvitations, Integer> {

    @Modifying
    @Query("UPDATE PendingInvitations d SET d.name_wishlist = :name_wishlist WHERE d.id_wishlist = :wishlistId")
    void updateWishlistName(@Param("wishlistId") Integer wishlistId, @Param("name_wishlist") String name_wishlist);

    @Modifying
    @Query("DELETE FROM PendingInvitations r WHERE r.id_pending_invitation = :id_pending_invitation")
    void deleteByIdInvitation(@Param("id_pending_invitation") Integer id_pending_invitation);


}
