package com.app.rest.controller.GetsEnums;

import com.app.domain.model.Categorys.Category;
import com.app.domain.model.Wishlist.Visibility;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @GetMapping
    public List<String> getAllCategories() {

        return Arrays.stream(Category.values())
                .map(Category::getCategoryDescription)
                .collect(Collectors.toList());
    }


}
