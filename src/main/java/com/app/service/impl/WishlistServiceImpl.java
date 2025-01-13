package com.app.service.impl;


import com.app.domain.model.DashboardEvents;
import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import com.app.domain.model.DashboardWishlists;
import com.app.domain.model.ResponseDTO.UpdateWishlistDTO;
import com.app.domain.model.ResponseDTO.WishlistDTO;
import com.app.domain.model.Users;
import com.app.domain.model.Utilities.MySubscriptions;
import com.app.domain.model.Wishlist.*;
import com.app.domain.repository.Products.DashboardProductsRepository;
import com.app.domain.repository.Wishlist.*;
import com.app.events.wishlist.WishlistUpdatedEvent;
import com.app.exception.BusinessRuleException;
import com.app.service.WishlistService;
import com.app.utils.AuthenticationUtils;
import com.app.utils.ValidateValuesEnums;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WishlistServiceImpl implements WishlistService {

    //TODA VEZ QUE ATUALIZAR/TIVER UM INSCRITO/LIKE/SHARE/COPIA NOVA, PRECISA ATUALIZAR
    // WISHLIST IN EVENTS DA CLASSE EVENTS.


    //AO ATUALIZAR A WISHLIST, EU POSSO PASSAR OUTRO EVENTO, APAGAR OQ TA E ADICIONAR UM NOVO EVENTO
    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    EventsInWishlistsRepository eventsInWishlistsRepository;
    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    WishlistsInEventsRepository wishlistsInEventsRepository;

    @Autowired
    DashboardRequestsSubscribersRepository dashboardRequestsSubscribersRepository;

    @Autowired
    MySubscriptionsRepository mySubscriptionsRepository;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    DashboardProductsRepository dashboardProductsRepository;

    @Override
    public Wishlist findById(Integer id_wishlist) {
        return wishlistRepository.findById(id_wishlist).orElseThrow(NoSuchElementException::new);
    }

    @Autowired
    EventsRepository eventsRepository;

    private int generateIdentityWishlist() {
        String identityWishlist = "";

        for (int i = 0; i < 6; i++) {
            int caractere = (int) (Math.random() * 10);
            identityWishlist += caractere;
        }

        System.out.println("NEW IDENTITY WISHLIST: " + identityWishlist);
        return Integer.parseInt(identityWishlist);
    }

    public void linkEventInWishlist(Wishlist newWishlist) {
        Users userAuthenticate = AuthenticationUtils.getAuthenticatedUser();

        if(newWishlist.getHaveLinkedEvent()) {
            validateExistEvent(newWishlist, userAuthenticate);
        }
    }
    @Transactional
    public void validateExistEvent(Wishlist newWishlist, Users userAuthenticate) {
        DashboardEvents existingEvents = userAuthenticate.getDashboardEvents();
        List<Events> existingEvent = existingEvents.getEvents();

        if (existingEvents == null || existingEvent == null || existingEvent.isEmpty()) {
            throw new BusinessRuleException("No events available to link to wishlist.", HttpStatus.BAD_REQUEST);
        }

        boolean eventFound = false;
        for (Events event : existingEvent) {
            if (newWishlist.getName_event_linked().trim().equalsIgnoreCase(event.getEvent_name().trim())) {
                EventsInWishlists eventsInWishlist = getEventsInWishlists(event, newWishlist);

                newWishlist.setEventsInWishlists(eventsInWishlist);

                eventFound = true;
                break;
            }
        }

        if (!eventFound) {
            throw new BusinessRuleException("The selected event is not available for linking.", HttpStatus.BAD_REQUEST);
        }
    }

    private EventsInWishlists getEventsInWishlists(Events event, Wishlist newWishlist) {
        EventsInWishlists eventsInWishlist = new EventsInWishlists();
        eventsInWishlist.setId_event(event.getId_event());
        eventsInWishlist.setEvent_name(event.getEvent_name());
        eventsInWishlist.setStatus(event.getStatus());
        eventsInWishlist.setLast_update_date(event.getLast_update_date());
        eventsInWishlist.setStart_date(event.getStart_date());
        eventsInWishlist.setEnd_date(event.getEnd_date());
        eventsInWishlist.setCreation_date(event.getCreation_date());

        eventsInWishlistsRepository.save(eventsInWishlist);

        newWishlist.setName_event_linked(event.getEvent_name());

        return eventsInWishlist;
    }

    @Override
    @Transactional
    public WishlistDTO createWishlist(Wishlist newWishlist, WishlistDTO wishlistDTO, Tags newTags, DashboardWishlists dashboardWishlists,
                                      EventsInWishlists newEventInWishlist, DashboardRequestsSubscribers dashboardRequestsSubscribers,
                                      DashboardProducts dashboardProducts) {

        Date dateFormat = new Date();

        validateFieldsWishlist(newWishlist);
        linkEventInWishlist(newWishlist);

        Wishlist wishlistCreated = new Wishlist(newWishlist.getWishlist_identity(),
                newWishlist.getWishlist_name(), newWishlist.getUrl_img(), newWishlist.getDescription(), newWishlist.getId_owner(),
                newWishlist.getUsername_owner(), newWishlist.getVisibility(),  newWishlist.getIsRequiredRequest(), newWishlist.getUrl_share(),
                newWishlist.getCategory(), newWishlist.getSub_category(), newWishlist.getStatus_wishlist(),
                dateFormat, newWishlist.getEnablesProductReservations(), newWishlist.getEnableProductsByRecommendation(),
                newWishlist.getIsACopiedWishlist(), newWishlist.getTags(), newWishlist.getDashboardWishlists(),
                newWishlist.getHaveLinkedEvent(), newWishlist.getEventsInWishlists(), newWishlist.getName_event_linked(),
                newWishlist.getDashboardRequestsSubscribers(), newWishlist.getDashboardAllProducts());

        wishlistCreated.setDashboardRequestsSubscribers(dashboardRequestsSubscribers);
        wishlistCreated.setDashboardAllProducts(dashboardProducts);

        Wishlist wishlistSaved = wishlistRepository.save(wishlistCreated);

        validateTags(wishlistSaved);
        validateFieldsDashboards(dashboardRequestsSubscribers, dashboardProducts, wishlistSaved);

        if(newWishlist.getName_event_linked() != null) {
            validateEvents(wishlistSaved);
        }

        return responseCreateWishlistDTO(wishlistSaved);
    }

    public void validateFieldsDashboards(DashboardRequestsSubscribers dashboardRequestsSubscribers, DashboardProducts dashboardProducts, Wishlist wishlistSaved) {
        validateFieldsSubscribersRequests(dashboardRequestsSubscribers, wishlistSaved);
        validateFieldsDashboardProducts(dashboardProducts, wishlistSaved);
    }

    public void validateFieldsDashboardProducts( DashboardProducts dashboardProducts, Wishlist wishlistSaved) {
        if(wishlistSaved != null) {
            dashboardProducts.setId_wishlist(wishlistSaved.getId_wishlist());

            dashboardProductsRepository.save(dashboardProducts);

        }
    }


    public void validateFieldsSubscribersRequests(DashboardRequestsSubscribers dashboardRequestsSubscriber, Wishlist wishlistSaved) {
        if(wishlistSaved != null) {
            dashboardRequestsSubscriber.setId_responsible_user(wishlistSaved.getId_owner());
            dashboardRequestsSubscriber.setUsername_responsible(wishlistSaved.getUsername_owner());
            dashboardRequestsSubscriber.setId_wishlist(wishlistSaved.getId_wishlist());
            dashboardRequestsSubscriber.setName_wishlist(wishlistSaved.getWishlist_name());
            dashboardRequestsSubscriber.setIdentity_wishlist(wishlistSaved.getWishlist_identity());

            dashboardRequestsSubscribersRepository.save(dashboardRequestsSubscriber);

        }
    }


    public void linkingWishlistInEvent(EventsInWishlists existingEvent, Wishlist wishlistSaved) {

        Integer idEvent = existingEvent.getId_event();

        Events existEventInWishlist = eventsRepository.findById(idEvent)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        if(existEventInWishlist != null) {
            getWishlistsInEvents(existEventInWishlist, wishlistSaved);
        }
    }

    private WishlistsInEvents getWishlistsInEvents(Events existEventInWishlist, Wishlist wishlistSaved) {

        WishlistsInEvents createNewWishlistInEvent = new WishlistsInEvents();
        createNewWishlistInEvent.setId_wishlist(wishlistSaved.getId_wishlist());
        createNewWishlistInEvent.setWishlist_name(wishlistSaved.getWishlist_name());
        createNewWishlistInEvent.setWishlist_identity(wishlistSaved.getWishlist_identity());
        createNewWishlistInEvent.setCreation_date(wishlistSaved.getCreation_date());
        createNewWishlistInEvent.setVisibility(wishlistSaved.getVisibility());
        createNewWishlistInEvent.setStatus(wishlistSaved.getStatus_wishlist());
        createNewWishlistInEvent.setCategory(wishlistSaved.getCategory());
        createNewWishlistInEvent.setStart_date(wishlistSaved.getStart_date());
        createNewWishlistInEvent.setEnd_date(wishlistSaved.getEnd_date());
        createNewWishlistInEvent.setCount_likes(wishlistSaved.getCount_likes());
        createNewWishlistInEvent.setCount_shares(wishlistSaved.getCount_shares());
        createNewWishlistInEvent.setCount_copies(wishlistSaved.getCount_copies());
        createNewWishlistInEvent.setCount_total_subscribers(wishlistSaved.getCount_total_subscribers());
        createNewWishlistInEvent.setEvent(existEventInWishlist);

        wishlistsInEventsRepository.save(createNewWishlistInEvent);

        return createNewWishlistInEvent;

    }

    @Override
    public UpdateWishlistDTO updateWishlist(Wishlist wishlistUpdated, Integer id_wishlist, UpdateWishlistDTO updateWishlistDTO) {

        Date dateLastUpdate = new Date();

        Wishlist selectedWishlist = findById(id_wishlist);

        validateFieldsWishlistUpdate(updateWishlistDTO, selectedWishlist);

        selectedWishlist.setWishlist_name(updateWishlistDTO.wishlist_name());
        selectedWishlist.setUrl_img(updateWishlistDTO.url_img());
        selectedWishlist.setDescription(updateWishlistDTO.description());
        selectedWishlist.setVisibility(updateWishlistDTO.visibility());
        selectedWishlist.setCategory(updateWishlistDTO.category());
        selectedWishlist.setSub_category(updateWishlistDTO.sub_category());
        selectedWishlist.setTags(updateWishlistDTO.tags());
        selectedWishlist.setStatus_wishlist(updateWishlistDTO.status_wishlist());
        selectedWishlist.setLast_update_date(dateLastUpdate);
        selectedWishlist.setEnableProductsByRecommendation(updateWishlistDTO.enableProductsByRecommendation());
        selectedWishlist.setEnablesProductReservations(updateWishlistDTO.enablesProductReservations());
        selectedWishlist.setUseEventDate(updateWishlistDTO.useEventDate());

        savingNewUpdatesInTheField(wishlistUpdated, selectedWishlist);

        Wishlist wishlistSaved = wishlistRepository.save(wishlistUpdated);

        eventPublisher.publishEvent(new WishlistUpdatedEvent(this, wishlistSaved));

        validateTags(wishlistSaved);
        updateWishlistInEvents(wishlistSaved);
        updateWishlistInOthers(wishlistSaved);


        return responseUpdateWishlistDTO(wishlistSaved);
    }

    public void updateWishlistInOthers( Wishlist wishlistSaved) {
        updateMySubscriptions(wishlistSaved);

    }

    public void updateMySubscriptions(Wishlist wishlistSaved) {
        //o count no MYSUBSCRIPTIONS nao esta atualizando tbm
        List<MySubscriptions> subscriptionsWishlist = mySubscriptionsRepository.findAll();

        for(MySubscriptions subs : subscriptionsWishlist) {
            if(wishlistSaved.getId_wishlist().equals(subs.getId_wishlist())) {
                subs.setName_wishlist(wishlistSaved.getWishlist_name());
                subs.setVisibility(Visibility.valueOf(wishlistSaved.getVisibility()));
                subs.setCategory(wishlistSaved.getCategory());

                mySubscriptionsRepository.saveAll(subscriptionsWishlist);
            }
        }
    }


    public void updateWishlistInEvents(Wishlist wishlistSaved) {
        EventsInWishlists existingEvent = wishlistSaved.getEventsInWishlists();

        if (existingEvent == null) {
            return;
        }

        Integer idEvent = existingEvent.getId_event();
        Events existEventInWishlist = eventsRepository.findById(idEvent)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        List<WishlistsInEvents> wishlists = existEventInWishlist.getWishlists();

        if (wishlists != null && !wishlists.isEmpty()) {
            WishlistsInEvents targetWishlist = wishlists.stream()
                    .filter(w -> w.getId_wishlist().equals(wishlistSaved.getId_wishlist()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Wishlist not found in event"));

            targetWishlist.setWishlist_name(wishlistSaved.getWishlist_name());
            targetWishlist.setVisibility(wishlistSaved.getVisibility());
            targetWishlist.setCategory(wishlistSaved.getCategory());
            targetWishlist.setStart_date(wishlistSaved.getStart_date());
            targetWishlist.setEnd_date(wishlistSaved.getEnd_date());
            targetWishlist.setStatus(wishlistSaved.getStatus_wishlist());

            wishlistsInEventsRepository.save(targetWishlist);
        } else {
            throw new BusinessRuleException("No wishlists found for the specified event.", HttpStatus.BAD_REQUEST);
        }
    }

    public UpdateWishlistDTO responseUpdateWishlistDTO(Wishlist wishlistSaved) {

        UpdateWishlistDTO UpdateDTO = new UpdateWishlistDTO(
                wishlistSaved.getWishlist_name(), wishlistSaved.getUrl_img(), wishlistSaved.getDescription(),
                wishlistSaved.getVisibility(),
                wishlistSaved.getIsRequiredRequest(),
                wishlistSaved.getCategory(), wishlistSaved.getSub_category(), wishlistSaved.getTags(),
                wishlistSaved.getStatus_wishlist(), wishlistSaved.getLast_update_date(),
                wishlistSaved.getEnablesProductReservations(), wishlistSaved.getEnableProductsByRecommendation(), wishlistSaved.getUseEventDate()
        );

        return UpdateDTO;
    }

    public void savingNewUpdatesInTheField(Wishlist wishlist, Wishlist selectedWishlist) {

        if(selectedWishlist.getWishlist_name() != null) {
            wishlist.setWishlist_name(selectedWishlist.getWishlist_name());
     }

        if (selectedWishlist.getUrl_img() != null) {
            wishlist.setUrl_img(selectedWishlist.getUrl_img());
        }

         if(selectedWishlist.getDescription() != null) {
             wishlist.setDescription(selectedWishlist.getDescription());
     }

        if(selectedWishlist.getVisibility() != null) {
            wishlist.setVisibility(selectedWishlist.getVisibility());
        }

        if(selectedWishlist.getIsRequiredRequest() != null) {
            wishlist.setIsRequiredRequest(selectedWishlist.getIsRequiredRequest());
        }

        if(selectedWishlist.getCategory() != null) {
            wishlist.setCategory(selectedWishlist.getCategory());
        }
        if(selectedWishlist.getSub_category() != null) {
            wishlist.setSub_category(selectedWishlist.getSub_category());
        }
        if(selectedWishlist.getTags() != null) {
            wishlist.setTags(selectedWishlist.getTags());
        }
        if(selectedWishlist.getStatus_wishlist() != null) {
            wishlist.setStatus_wishlist(selectedWishlist.getStatus_wishlist());
        }

        if(selectedWishlist.getEnablesProductReservations() != null) {
            wishlist.setEnablesProductReservations(selectedWishlist.getEnablesProductReservations());
        }

        if(selectedWishlist.getEnableProductsByRecommendation() != null) {
            wishlist.setEnableProductsByRecommendation(selectedWishlist.getEnableProductsByRecommendation());
        }

        if(selectedWishlist.getUseEventDate() != null) {
            wishlist.setUseEventDate(selectedWishlist.getUseEventDate());
        }

        if(selectedWishlist.getLast_update_date() != null) {
            wishlist.setLast_update_date(selectedWishlist.getLast_update_date());
        }
    }

    public WishlistDTO responseCreateWishlistDTO(Wishlist wishlistSaved) {

        return new WishlistDTO(
                wishlistSaved.getWishlist_identity(),
                wishlistSaved.getWishlist_name(), wishlistSaved.getUrl_img(), wishlistSaved.getDescription(), wishlistSaved.getId_owner(),
                wishlistSaved.getUsername_owner(), wishlistSaved.getVisibility(), wishlistSaved.getIsRequiredRequest(), wishlistSaved.getUrl_share(),
                wishlistSaved.getCategory(), wishlistSaved.getSub_category(), wishlistSaved.getTags(), wishlistSaved.getStatus_wishlist(),
                wishlistSaved.getCreation_date(), wishlistSaved.getEnablesProductReservations(),
                wishlistSaved.getEnableProductsByRecommendation(), wishlistSaved.getIsACopiedWishlist(),
                wishlistSaved.getDashboardWishlists().getId_dashboard_wishlists(), wishlistSaved.getHaveLinkedEvent(),
                wishlistSaved.getName_event_linked(), wishlistSaved.getEventsInWishlists(),
                wishlistSaved.getDashboardRequestsSubscribers().getId_dashboard_requests(), wishlistSaved.getDashboardAllProducts().getId_dashboard_products()
        );

    }

    public void validateFieldsWishlistUpdate( UpdateWishlistDTO updateWishlistDTO, Wishlist selectedWishlist) {

        AuthenticationUtils.getAuthenticatedUser();

        Integer idOwner = selectedWishlist.getId_owner();

        AuthenticationUtils.validateUser(() -> idOwner);

       // validateValuesVisibility(newWishlist);
        ValidateValuesEnums.validateCategory(() -> updateWishlistDTO.category());
        ValidateValuesEnums.validateStatus(() -> updateWishlistDTO.status_wishlist());

    }

    public void validateFieldsWishlist(Wishlist newWishlist) {

        Users authenticateUser = AuthenticationUtils.getAuthenticatedUser();

        newWishlist.setId_owner(authenticateUser.getId_user());
        newWishlist.setUsername_owner(authenticateUser.getUsername());
        newWishlist.setDashboardWishlists(authenticateUser.getDashboardWishlists());
        newWishlist.setWishlist_identity(generateIdentityWishlist());

        validateValuesVisibility(newWishlist);
        ValidateValuesEnums.validateCategory(() -> newWishlist.getCategory());
        ValidateValuesEnums.validateStatus(() -> newWishlist.getStatus_wishlist());

    }

    public void validateTags(Wishlist wishlistSaved) {
        for (Tags tag : wishlistSaved.getTags()) {
            tag.setWishlist(wishlistSaved);
            tagsRepository.save(tag);
        }
    }

    public void validateEvents(Wishlist wishlistSaved) {

        EventsInWishlists existingEvent = wishlistSaved.getEventsInWishlists();

        if (existingEvent == null) {
            throw new BusinessRuleException("Event in Wishlist is not available.", HttpStatus.BAD_REQUEST);
        }

        existingEvent.setWishlist(wishlistSaved);
        eventsInWishlistsRepository.save(existingEvent);

        linkingWishlistInEvent(existingEvent, wishlistSaved);
    }

    public void validateValuesVisibility(Wishlist newWishlist) {

        List<String> existingVisibility = Arrays.stream(Visibility.values())
                .map(Visibility::getVisibility_description)
                .toList();

        if (existingVisibility.contains(newWishlist.getVisibility())) {
            validateRequired(newWishlist);

        } else {
            throw new BusinessRuleException("The visibility (" + newWishlist.getVisibility() + ") not is valid.", HttpStatus.BAD_REQUEST);
        }
    }

    public void validateRequired(Wishlist newWishlist) {

        if (newWishlist.getVisibility().equals(Visibility.PUBLIC.getVisibility_description())) {
            newWishlist.setIsRequiredRequest(false);
        }
        else if (newWishlist.getVisibility().equals(Visibility.JUSTME.getVisibility_description()) ||
                newWishlist.getVisibility().equals(Visibility.PRIVATE.getVisibility_description())) {
            newWishlist.setIsRequiredRequest(true);
        } else if (newWishlist.getVisibility().equals(Visibility.ONLYFRIENDS.getVisibility_description())) {
            newWishlist.setIsRequiredRequest(true);
        }
        //PENSAR NA LOGICA: SE FOR AMIGO PODE SE INSCREVER
}


}
