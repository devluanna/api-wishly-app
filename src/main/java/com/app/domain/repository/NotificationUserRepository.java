package com.app.domain.repository;

import com.app.domain.model.NotificationsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationsUser, Integer> {
}
