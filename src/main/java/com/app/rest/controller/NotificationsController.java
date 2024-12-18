package com.app.rest.controller;

import com.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/notifications")

public class NotificationsController {

    @Autowired
    NotificationService notificationService;

    @PutMapping("/{id_notification}/viewed")
    public ResponseEntity<?> notificationMarkAsViewed(@PathVariable Integer id_notification) {

        return notificationService.notificationViewd(id_notification);

    }


}