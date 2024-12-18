package com.app.utils;

import com.app.domain.model.Connections;
import com.app.domain.model.Users;
import com.app.domain.model.Wishlist.Visibility;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.repository.User.UserRepository;
import com.app.exception.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VisibilityHandler {

    @Autowired
    UserRepository userRepository;

    public boolean canSubscribe(Users user, Wishlist wishlist) {
        String visibility = wishlist.getVisibility();
        return visibility.equals(Visibility.PUBLIC.getVisibility_description())
                || (visibility.equals(Visibility.ONLYFRIENDS.getVisibility_description())
                && isFriend(user, wishlist));
    }

    public boolean requiresRequest(Wishlist wishlist) {
        return wishlist.getVisibility().equals(Visibility.PRIVATE.getVisibility_description());
    }

    private boolean isFriend(Users user, Wishlist wishlist) {
        Integer idOwner = wishlist.getId_owner();

        Users existUserOwner = userRepository.findById(idOwner)
                .orElseThrow(() -> new EntityNotFoundException("User not Found!"));

        if (existUserOwner != null) {
            List<Connections> listConnections = existUserOwner.getConnectionsDashboard().getConnections();

            return listConnections.stream()
                    .anyMatch(conn -> user.getId_user().equals(conn.getId_user_connection()));
        }

        throw new BusinessRuleException("User not found", HttpStatus.BAD_REQUEST);
    }
}