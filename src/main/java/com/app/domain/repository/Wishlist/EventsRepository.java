package com.app.domain.repository.Wishlist;

import com.app.domain.model.Wishlist.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventsRepository extends JpaRepository<Events, Integer> {
    @Query("SELECT e FROM Events e WHERE e.event_name = :event_name")
    Optional<Events> findByEventName(@Param("event_name") String event_name);

    @Modifying
    @Query("UPDATE Events d SET d.username_owner = :username_owner WHERE d.id_owner = :userIdOwner")
    void updateUsernameOwner(@Param("userIdOwner") Integer userIdOwner, @Param("username_owner") String username_owner);
}
