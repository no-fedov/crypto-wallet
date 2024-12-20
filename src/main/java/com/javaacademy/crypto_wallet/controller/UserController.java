package com.javaacademy.crypto_wallet.controller;

import com.javaacademy.crypto_wallet.dto.UserCreateDto;
import com.javaacademy.crypto_wallet.dto.UserUpdatePasswordDto;
import com.javaacademy.crypto_wallet.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("POST: user/signup, new user = {}", userCreateDto);
        userService.save(userCreateDto);
        log.info("User {} is saved", userCreateDto);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePassword(@RequestBody @Valid UserUpdatePasswordDto userUpdatePasswordDto) {
        log.info("POST: user/reset-password, updated user = {}", userUpdatePasswordDto);
        userService.resetPassword(userUpdatePasswordDto);
        log.info("User {} update password", userUpdatePasswordDto);
    }
}
