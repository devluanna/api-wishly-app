package com.app.domain.repository.Wishlist;

import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRequestsSubscribersRepository extends JpaRepository<DashboardRequestsSubscribers, Integer> {

    @Modifying
    @Query("UPDATE DashboardRequestsSubscribers d SET d.name_wishlist = :name_wishlist WHERE d.id_wishlist = :wishlistId")
    void updateWishlistName(@Param("wishlistId") Integer wishlistId, @Param("name_wishlist") String name_wishlist);

    @Modifying
    @Query("UPDATE DashboardRequestsSubscribers d SET d.username_responsible = :usernameResponsible WHERE d.id_responsible_user = :userId")
    void updateUsername(@Param("userId") Integer userId, @Param("usernameResponsible") String usernameResponsible);

}
