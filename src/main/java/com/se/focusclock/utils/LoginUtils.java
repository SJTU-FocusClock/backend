package com.se.focusclock.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUtils {
    public static String getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                return null;
            }
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
//                Principal principal = (Principal) authentication.getPrincipal();
                return authentication.getName();
            }
        }
        return null;
    }
}