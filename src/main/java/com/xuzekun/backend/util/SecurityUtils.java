package com.xuzekun.backend.util;

import com.xuzekun.backend.exception.BusinessException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;

public class SecurityUtils {

    public static String getCurrentPublicAddress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        } else {
            throw new BusinessException("Failed to get user information!");
        }
    }

    public static String getCurrentUserSign() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtClaimAccessor principal = (JwtClaimAccessor) authentication.getPrincipal();
        return principal.getClaimAsString("iss");
    }

}
