package com.app.domain.repository.Wishlist;

import com.app.domain.model.Wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    @Modifying
    @Query("UPDATE Wishlist d SET d.username_owner = :username_owner WHERE d.id_owner = :userIdOwner")
    void updateUsernameOwner(@Param("userIdOwner") Integer userIdOwner, @Param("username_owner") String username_owner);

}
