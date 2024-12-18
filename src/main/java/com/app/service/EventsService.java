package com.app.service;

import com.app.domain.model.DashboardEvents;
import com.app.domain.model.ResponseDTO.EventsUpdateDTO;
import com.app.domain.model.Wishlist.Events;
import org.springframework.stereotype.Service;

@Service
public interface EventsService {
    Events createEvent(Events events, DashboardEvents dashboardEvents);

    EventsUpdateDTO updateEvent(Events event, Integer id_event, EventsUpdateDTO eventsUpdateDTO);

    Events findById(Integer id_event);
}
