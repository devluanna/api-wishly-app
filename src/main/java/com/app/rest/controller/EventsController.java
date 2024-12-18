package com.app.rest.controller;

import com.app.domain.model.DashboardEvents;
import com.app.domain.model.ResponseDTO.EventsUpdateDTO;
import com.app.domain.model.ResponseDTO.UpdateWishlistDTO;
import com.app.domain.model.Wishlist.Events;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {

    @Autowired
    EventsService eventsService;
    @PostMapping("/create")
    public ResponseEntity createEvent(@RequestBody Events events, DashboardEvents dashboardEvents) {

        Events eventsCreated = eventsService.createEvent(events, dashboardEvents);

        return ResponseEntity.ok(eventsCreated);

    }

    @PutMapping("/update/{id_event}")
    public ResponseEntity updateEvent(@PathVariable Integer id_event, @RequestBody EventsUpdateDTO eventsUpdateDTO) {

        if (id_event == null) {
            System.out.println("Event not found!");
            return ResponseEntity.badRequest().build();
        }

        Events event = eventsService.findById(id_event);

        EventsUpdateDTO eventSaved = eventsService.updateEvent(event, id_event, eventsUpdateDTO);

        return ResponseEntity.ok(eventSaved);
    }

}
