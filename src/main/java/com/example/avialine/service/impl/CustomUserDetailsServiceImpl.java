package com.example.avialine.service.impl;

import com.example.avialine.exception.UserNotFoundException;
import com.example.avialine.messages.ApiErrorMessage;
import com.example.avialine.model.entity.User;
import com.example.avialine.repo.UserRepo;
import com.example.avialine.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_EMAIL_MESSAGE.getMessage(email)));
        return user;
    }


}
