package com.nnt.hotelbooking.common.currentUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class CustomUserPrincipal {

    private Long authId;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
}
