package com.app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    ResponseEntity<?> notificationViewd(Integer id_notification);
}
