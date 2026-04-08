package com.nnt.hotelbooking.common.currentUser;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProviderImpl implements CurrentUserProvider {

    public Long getAuthId() {
        return getPrincipal().getAuthId();
    }

    private CustomUserPrincipal getPrincipal() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {
            throw new RuntimeException("Unauthenticated");
        }

        return principal;
    }


}

