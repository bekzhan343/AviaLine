package com.example.avialine.security.util;

import com.example.avialine.dto.response.DetailErrorResponse;
import com.example.avialine.messages.ApiErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String authHeader = request.getHeader("Authorization");
        String message;

        if (authHeader == null || authHeader.isEmpty()){
            message = ApiErrorMessage.NO_PROVIDED_ACCOUNT_MESSAGE.getMessage();
        }else {
            message = ApiErrorMessage.INVALID_CREDENTIALS_MESSAGE.getMessage();
        }

        response.getWriter().write(mapper.writeValueAsString(
                        new DetailErrorResponse(
                                message
                        )
                )
        );


    }
}
