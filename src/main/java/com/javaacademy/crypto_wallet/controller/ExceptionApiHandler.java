package com.javaacademy.crypto_wallet.controller;

import com.javaacademy.crypto_wallet.dto.ErrorResponseDto;
import com.javaacademy.crypto_wallet.exception.EntityAlreadyExistException;
import com.javaacademy.crypto_wallet.exception.EntityNotFoundException;
import com.javaacademy.crypto_wallet.exception.InsufficientFundsException;
import com.javaacademy.crypto_wallet.exception.InvalidPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler({EntityAlreadyExistException.class,
            InsufficientFundsException.class,
            InvalidPasswordException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto entityExceptionHandler(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto notFoundExceptionHandler(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
}
