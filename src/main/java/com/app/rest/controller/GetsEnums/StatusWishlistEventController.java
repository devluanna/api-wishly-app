package com.app.rest.controller.GetsEnums;


import com.app.domain.model.Wishlist.StatusWishlistEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/status-wishlist-event")
public class StatusWishlistEventController {

    @GetMapping
    public List<String> getAllStatusWishlistEvent() {

        return Arrays.stream(StatusWishlistEvent.values())
                .map(StatusWishlistEvent::getStatusWishlistEvent)
                .collect(Collectors.toList());

    }

}
