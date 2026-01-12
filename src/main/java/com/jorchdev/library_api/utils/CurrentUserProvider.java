package com.jorchdev.library_api.utils;

import com.jorchdev.library_api.exceptions.UnauthorizedException;
import com.jorchdev.library_api.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public CustomUserDetails getCurrentUser() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        Object principal = auth.getPrincipal();

        if (!(principal instanceof CustomUserDetails)) {
            throw new UnauthorizedException("User invalid");
        }

        return (CustomUserDetails) principal;
    }
}
