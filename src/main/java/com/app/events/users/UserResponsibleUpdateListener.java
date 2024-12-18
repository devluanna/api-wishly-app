package com.app.events.users;

import com.app.domain.model.Users;
import com.app.domain.repository.User.ConnectionsDashboardRepository;
import com.app.domain.repository.User.DashboardEventsRepository;
import com.app.domain.repository.User.DashboardWishlistsRepository;
import com.app.domain.repository.Wishlist.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserResponsibleUpdateListener {

    @Autowired
    DashboardWishlistsRepository dashboardWishlistsRepository;
    @Autowired
    DashboardEventsRepository dashboardEventsRepository;
    @Autowired
    DashboardRequestsAndPendingRepository dashboardRequestsAndPendingRepository;
    @Autowired
    ConnectionsDashboardRepository connectionsDashboardRepository;
    @Autowired
    DashboardRequestsSubscribersRepository dashboardRequestsSubscribersRepository;


    @EventListener
    @Transactional
    public void handleUserResponsibleUpdatedEvent(UserUpdatedEvent event) {
        Users updatedUser = event.getUsers();
        String newUserName = updatedUser.getUsername();
        Integer userId = updatedUser.getId_user();

        dashboardRequestsSubscribersRepository.updateUsername(userId, newUserName);
        dashboardWishlistsRepository.updateUsername(userId, newUserName);
        dashboardEventsRepository.updateUsername(userId, newUserName);
        connectionsDashboardRepository.updateUsername(userId, newUserName);
        dashboardRequestsAndPendingRepository.updateUsername(userId, newUserName);

    }
}
