package com.app.service.impl;

import com.app.domain.model.NotificationsUser;
import com.app.domain.model.ResponseDTO.SuccessResponse;
import com.app.domain.model.Users;
import com.app.domain.repository.User.NotificationUserRepository;
import com.app.domain.repository.User.UserRepository;
import com.app.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationUserRepository notificationsUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void sendNotification(Integer userId, String notificationName, String notificationDescription, boolean isReminder) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        NotificationsUser notification = new NotificationsUser();
        notification.setUsername(user.getUsername());
        notification.setId_dashboard_user(user.getConnectionsDashboard().getId_dashboard());
        notification.setNotification_name(notificationName);
        notification.setNotification_description(notificationDescription);
        notification.setDate_of_notification(new Date());
        notification.setNotification_reminder(isReminder);
        notification.setNotificationWasViewed(false);
        notification.setUsers(user);

        notificationsUserRepository.save(notification);

        user.addNewNotification(notification);
        user.setCount_notifications_total(user.getNotificationsUsers().size());
        user.setCount_notifications_unread(user.getNotificationsUsers().size());
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> notificationViewd(Integer id_notification) {

        //Verificar se o id existe ok
        // precisa verificar se o status dele e false
        //depois atualizar o status notificacao
        //salvar
        //atualizar counts do Users

        NotificationsUser existNotification = notificationsUserRepository.findById(id_notification).orElseThrow(() ->
                new EntityNotFoundException("Notification not found"));

       if(!existNotification.isNotificationWasViewed()) {

           existNotification.setNotificationWasViewed(true);
           notificationsUserRepository.save(existNotification);

           Users user = existNotification.getUsers();
           user.setCount_notifications_unread(user.getCount_notifications_unread() - 1);
           user.setCount_notifications_read(user.getCount_notifications_read() + 1);

           userRepository.save(user);

       }

        return ResponseEntity.ok(new SuccessResponse("Notificacao foi visualizada!"));

    }
}