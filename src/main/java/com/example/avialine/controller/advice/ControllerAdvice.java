package com.example.avialine.controller.advice;

import com.example.avialine.dto.response.DefaultResponse;
import com.example.avialine.dto.response.DetailErrorResponse;
import com.example.avialine.dto.response.GlobalErrorResponse;
import com.example.avialine.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.*;


@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DefaultResponse> handlerUserNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(404).body(
                new DefaultResponse(
                        false,
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<DetailErrorResponse> handlerInvalidCredentialsException(InvalidCredentialsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new DetailErrorResponse(e.getMessage())
        );
    }

    @ExceptionHandler(PasswordDoNotMatchException.class)
    public ResponseEntity<String> handlerPasswordDoNotMatchException(PasswordDoNotMatchException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handlerUserAlreadyExistsException(UserAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handlerInvalidTokenException(InvalidTokenException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handlerTokenExpiredException(TokenExpiredException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handlerRoleNotFoundException(RoleNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handlerUnauthorizedException(UnauthorizedException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<String> handlerRoleAlreadyExistsException(RoleAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<DefaultResponse> handlerInvalidVerificationCodeException(InvalidVerificationCodeException e){


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new DefaultResponse(
                        false,
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GlobalErrorResponse> handlerValidationException(ValidationException e){
        return ResponseEntity.status(400).body(
                new GlobalErrorResponse(
                        false,
                        e.getMessage(),
                        e.getErrors()

                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            WebRequest request
    ){

        String path = ((ServletWebRequest) request) .getRequest().getRequestURI();

        List<String> simpleEndPoints = List.of("/auth/user/confirm-code");

        if (simpleEndPoints.contains(path)) {
            String message = e.getBindingResult().getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("Validation error");

            return ResponseEntity.status(400).body(new DefaultResponse(
                    false,
                    message
                    )
            );
        }




        Map<String, List<String>> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError ->
        {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
        }
        );

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse(
                false,
                "error",
                errors
        );

        return ResponseEntity.status(400).body(globalErrorResponse);
    }

    @ExceptionHandler(CodeAlreadyVerifiedException.class)
    public ResponseEntity<DefaultResponse> handlerCodeAlreadyVerifiedException(CodeAlreadyVerifiedException e){
        return ResponseEntity.status(400).body(
                new DefaultResponse(
                        false,
                        e.getMessage()
                )
        );
    }

}
