package com.example.avialine.service.impl;

import com.example.avialine.exception.InvalidCredentialsException;
import com.example.avialine.enums.ApiErrorMessage;
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
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        User user = userRepo.findActiveUserByPhone(phone)
                .orElseThrow(() -> new InvalidCredentialsException(ApiErrorMessage.USER_NOT_FOUND_BY_PHONE_MESSAGE.getMessage(phone)));
        return user;
    }


}
