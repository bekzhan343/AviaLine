package com.example.avialine.security.util;

import com.example.avialine.exception.UnauthorizedException;
import com.example.avialine.messages.ApiErrorMessage;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Authentication requireAuthentication(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            throw new UnauthorizedException(ApiErrorMessage.UNAUTHORIZED_MESSAGE.getMessage());
        }

        return auth;
    }
}
