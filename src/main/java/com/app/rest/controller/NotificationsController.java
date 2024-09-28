package com.app.rest.controller;

import com.app.domain.model.NotificationsUser;
import com.app.domain.repository.NotificationUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/notifications")
public class NotificationsController {


    @Autowired
    private NotificationUserRepository notificationsUserRepository;

    @PutMapping("/{id_notification}/view")
    public ResponseEntity<?> markNotificationAsViewed(@PathVariable Integer id_notification) {

        NotificationsUser notification = notificationsUserRepository.findById(id_notification)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        notification.setNotificationWasViewed(true);
        notificationsUserRepository.save(notification);

        return ResponseEntity.ok().build();
    }

}
