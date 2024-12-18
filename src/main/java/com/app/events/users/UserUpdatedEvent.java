package com.app.events.users;

import com.app.domain.model.Users;
import org.springframework.context.ApplicationEvent;

public class UserUpdatedEvent extends ApplicationEvent {

    private final Users users;

    public UserUpdatedEvent(Object source, Users users) {
        super(source);
        this.users = users;
    }

    public Users getUsers() {
        return users;
    }

}
