package com.javaacademy.crypto_wallet.controller;

import com.javaacademy.crypto_wallet.dto.ErrorResponseDto;
import com.javaacademy.crypto_wallet.dto.UserCreateDto;
import com.javaacademy.crypto_wallet.dto.UserUpdatePasswordDto;
import com.javaacademy.crypto_wallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Контроллер для работы с пользователями",
        description = "Содержит методы для работы с пользователями")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Operation(
            summary = "Создает нового пользователя",
            description = "Сохраняет нового пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Логин занят",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("POST: user/signup, new user = {}", userCreateDto);
        userService.save(userCreateDto);
        log.info("User {} is saved", userCreateDto);
    }

    @Operation(
            summary = "Обновляет пароль пользователю",
            description = "Перезаписывает сатрый пароль на новый")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный пароль",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePassword(@RequestBody @Valid UserUpdatePasswordDto userUpdatePasswordDto) {
        log.info("POST: user/reset-password, updated user = {}", userUpdatePasswordDto);
        userService.resetPassword(userUpdatePasswordDto);
        log.info("User {} update password", userUpdatePasswordDto);
    }
}
