package com.app.utils;
import java.util.function.Supplier;

import com.app.domain.model.Users;
import com.app.exception.BusinessRuleException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    public static Users validateUser(Supplier<Integer> comparisonValueSupplier) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessRuleException("Authentication is required", HttpStatus.BAD_REQUEST);
        }

        Users authenticatedUser = (Users) authentication.getPrincipal();

        if (!authenticatedUser.getId_user().equals(comparisonValueSupplier.get())) {
            throw new BusinessRuleException("Access denied!", HttpStatus.BAD_REQUEST);
        }

        return authenticatedUser;
}
}
