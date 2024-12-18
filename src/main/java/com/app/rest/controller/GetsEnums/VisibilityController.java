package com.app.rest.controller.GetsEnums;


import com.app.domain.model.Wishlist.Visibility;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("api/v1/visibility")
public class VisibilityController {

    @GetMapping
    public List<String> getAllVisibility() {

        return Arrays.stream(Visibility.values())
                .map(Visibility::getVisibility_description)
                .collect(Collectors.toList());
    }


}
