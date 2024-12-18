package com.app.utils;

import com.app.domain.model.Categorys.Category;
import com.app.domain.model.Wishlist.StatusSubscribers;
import com.app.domain.model.Wishlist.StatusWishlistEvent;
import com.app.exception.BusinessRuleException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;


public class ValidateValuesEnums {

    public static void validateCategory(Supplier<String> comparisonValueSupplier) {
        List<String> existCategory = Arrays.stream(Category.values())
                .map(Category::getCategoryDescription)
                .toList();

        if (!existCategory.contains(comparisonValueSupplier.get())) {
            throw new BusinessRuleException("The category (" + comparisonValueSupplier.get() + ") not is valid.", HttpStatus.BAD_REQUEST);
        }
    }

    public static void validateStatus(Supplier<String> comparisonValueSupplier) {
        List<String> existStatus = Arrays.stream(StatusWishlistEvent.values())
                .map(StatusWishlistEvent::getStatusWishlistEvent)
                .toList();

        if (!existStatus.contains(comparisonValueSupplier.get())) {
            throw new BusinessRuleException("The Status (" + comparisonValueSupplier.get() + ") not is valid.", HttpStatus.BAD_REQUEST);
        }
    }

    public static void statusSubscriber(Supplier<String> comparisonValueSupplier) {
        List<String> existStatusSubscriber = Arrays.stream(StatusSubscribers.values())
                .map(StatusSubscribers::getStatus_wishlist_subscribers)
                .toList();

        if (!existStatusSubscriber.contains(comparisonValueSupplier.get())) {
            throw new BusinessRuleException("The Status Subscriber (" + comparisonValueSupplier.get() + ") not is valid.", HttpStatus.BAD_REQUEST);
        }
    }

}
