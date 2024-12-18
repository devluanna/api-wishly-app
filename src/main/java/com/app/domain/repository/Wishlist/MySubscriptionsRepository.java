package com.app.domain.repository.Wishlist;

import com.app.domain.model.Utilities.MySubscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MySubscriptionsRepository extends JpaRepository<MySubscriptions, Integer> {

    @Query("SELECT ms FROM MySubscriptions ms WHERE ms.id_wishlist = :id_wishlist")
    List<MySubscriptions> findById_wishlist(@Param("id_wishlist") Integer id_wishlist);

}
