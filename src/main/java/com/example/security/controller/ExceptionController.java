package com.example.security.controller;

import com.example.security.dto.ApiErrorDTO;
import com.example.security.exception.AccessDeniedException;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages={ "com.aktios.appestablecimientos.controller" })
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException.class)
    public ApiErrorDTO signatureException(InvalidRequestException e) {
        return new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiErrorDTO notFoundException(EntityNotFoundException e) {
        return new ApiErrorDTO(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiErrorDTO accessDeniedException(AccessDeniedException e) {
        return new ApiErrorDTO(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }
}