package com.app.service.impl;

import com.app.domain.model.DashboardEvents;
import com.app.domain.model.ResponseDTO.EventsUpdateDTO;
import com.app.domain.model.ResponseDTO.UpdateWishlistDTO;
import com.app.domain.model.Users;
import com.app.domain.model.Wishlist.Events;
import com.app.domain.model.Wishlist.EventsInWishlists;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.model.Wishlist.WishlistsInEvents;
import com.app.domain.repository.Wishlist.EventsInWishlistsRepository;
import com.app.domain.repository.Wishlist.EventsRepository;
import com.app.domain.repository.Wishlist.WishlistRepository;
import com.app.exception.BusinessRuleException;
import com.app.service.EventsService;
import com.app.utils.AuthenticationUtils;
import com.app.utils.ValidateValuesEnums;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    EventsInWishlistsRepository eventsInWishlistsRepository;

    @Autowired
    WishlistRepository wishlistRepository;


    @Override
    @Transactional
    public Events createEvent(Events events, DashboardEvents dashboardEvents) {

        //fluxo do front: Criar evento > inicialmente vai aparecer na wishlist "Nao possui nenhuma wishlist vinculada".
        // Ir na wishlist que deseja e vincular ao evento, se vincular, atualizara os campos: Nome wishlist, identifier e ID wishlist no evento.
        // Na wishlist, vai ser atualizado os campos: NOME EVENTO e caso ele queira as datas INICIO e FIM.

        Users userAuthenticate = AuthenticationUtils.getAuthenticatedUser();

        Integer id_user_authz = userAuthenticate.getId_user();
        String username_authz= userAuthenticate.getUsername();

        Date formatDate = new Date();

        Events newEvent = new Events(
         events.getEvent_name(), events.getEvent_description(), events.getCategory(), id_user_authz, username_authz,
                events.getStatus(), formatDate, events.getStart_date(), events.getEnd_date(), events.getDashboardEvents()
        );

        newEvent.setDashboardEvents(userAuthenticate.getDashboardEvents());

        validateEnumsCreate(newEvent);

        Events eventsSaved = eventsRepository.save(newEvent);

        return eventsSaved;
    }

    public void validateEnumsCreate(Events newEvent) {

        ValidateValuesEnums.validateCategory(() -> newEvent.getCategory());
        ValidateValuesEnums.validateStatus(() -> newEvent.getStatus());
    }

    @Override
    public EventsUpdateDTO updateEvent(Events event, Integer id_event, EventsUpdateDTO eventsUpdateDTO) {
        Date dateLastUpdate = new Date();

        AuthenticationUtils.getAuthenticatedUser();
        Events selectedEvent = findById(id_event);

        Integer idOwner = selectedEvent.getId_owner();

        AuthenticationUtils.validateUser(() -> idOwner);

        selectedEvent.setEvent_name(eventsUpdateDTO.event_name());
        selectedEvent.setEvent_description(eventsUpdateDTO.event_description());
        selectedEvent.setCategory(eventsUpdateDTO.category());
        selectedEvent.setStatus(eventsUpdateDTO.status());
        selectedEvent.setStart_date(eventsUpdateDTO.start_date());
        selectedEvent.setEnd_date(eventsUpdateDTO.end_date());;
        selectedEvent.setLast_update_date(dateLastUpdate);

        savingNewUpdatesInTheFieldEvent(event, selectedEvent);

        validateEnumsUpdate(eventsUpdateDTO);

        Events eventSaved = eventsRepository.save(event);

        updateEventsInWishlists(eventSaved);

        return responseUpdateEventDTO(eventSaved);
    }

    public void updateEventsInWishlists(Events eventSaved) {
        List<WishlistsInEvents> existingWishlists = eventSaved.getWishlists();

        if (existingWishlists == null || existingWishlists.isEmpty()) {
            return;
        }

        for (WishlistsInEvents wishlistInEvent : existingWishlists) {
            Integer wishlistId = wishlistInEvent.getId_wishlist();

            Wishlist wishlist = wishlistRepository.findById(wishlistId)
                    .orElseThrow(() -> new EntityNotFoundException("Wishlist not found for id: " + wishlistId));

            EventsInWishlists existingEventInWishlist = wishlist.getEventsInWishlists();

            if (existingEventInWishlist != null && existingEventInWishlist.getId_event().equals(eventSaved.getId_event())) {
                existingEventInWishlist.setEvent_name(eventSaved.getEvent_name());
                existingEventInWishlist.setStatus(eventSaved.getStatus());
                existingEventInWishlist.setStart_date(eventSaved.getStart_date());
                existingEventInWishlist.setEnd_date(eventSaved.getEnd_date());
                existingEventInWishlist.setLast_update_date(eventSaved.getLast_update_date());

                eventsInWishlistsRepository.save(existingEventInWishlist);
            }
        }
    }

    public void savingNewUpdatesInTheFieldEvent(Events event, Events selectedEvent) {
        if(selectedEvent.getEvent_name() != null) {
            event.setEvent_name(selectedEvent.getEvent_name());
        }

        if (selectedEvent.getEvent_description() != null) {
            event.setEvent_description(selectedEvent.getEvent_description());
        }

        if(selectedEvent.getCategory() != null) {
            event.setCategory(selectedEvent.getCategory());
        }

        if(selectedEvent.getStatus() != null) {
            event.setStatus(selectedEvent.getStatus());
        }

        if(selectedEvent.getStart_date() != null) {
            event.setStart_date(selectedEvent.getStart_date());
        }

        if(selectedEvent.getEnd_date() != null) {
            event.setEnd_date(selectedEvent.getEnd_date());
        }
        if(selectedEvent.getLast_update_date() != null) {
            event.setLast_update_date(selectedEvent.getLast_update_date());
        }
    }

    public void validateEnumsUpdate(EventsUpdateDTO eventsUpdateDTO) {

        ValidateValuesEnums.validateCategory(() -> eventsUpdateDTO.category());
        ValidateValuesEnums.validateStatus(() -> eventsUpdateDTO.status());
    }


    public EventsUpdateDTO responseUpdateEventDTO(Events eventSaved) {

        EventsUpdateDTO UpdateDTO = new EventsUpdateDTO(
                eventSaved.getEvent_name(), eventSaved.getEvent_description(), eventSaved.getCategory(),
                eventSaved.getStatus(),
                eventSaved.getStart_date(),
                eventSaved.getEnd_date(),eventSaved.getLast_update_date()
        );

        return UpdateDTO;
    }
    @Override
    public Events findById(Integer id_event) {
        return eventsRepository.findById(id_event).orElseThrow(NoSuchElementException::new);
    }


}
