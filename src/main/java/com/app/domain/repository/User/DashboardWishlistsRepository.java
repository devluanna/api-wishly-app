package com.app.domain.repository.User;

import com.app.domain.model.DashboardWishlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardWishlistsRepository extends JpaRepository<DashboardWishlists, Integer> {

    @Modifying
    @Query("UPDATE DashboardWishlists d SET d.responsible_username = :responsible_username WHERE d.id_responsible_user = :userId")
    void updateUsername(@Param("userId") Integer userId, @Param("responsible_username") String responsible_username);

}
