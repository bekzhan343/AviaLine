package com.example.avialine.security.util;

import com.example.avialine.exception.UnauthorizedException;
import com.example.avialine.messages.ApiErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static Authentication requireAuthentication(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            log.error("Auth required");
            throw new UnauthorizedException(ApiErrorMessage.UNAUTHORIZED_MESSAGE.getMessage());
        }

        return auth;
    }
}
