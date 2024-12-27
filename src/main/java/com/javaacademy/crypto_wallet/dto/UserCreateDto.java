package com.javaacademy.crypto_wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Представления для создания пользователя")
public class UserCreateDto {

    @NotNull
    @Schema(description = "уникальный логин пользователя")
    private String login;

    @NotNull
    @Schema(description = "электронная почта пользователя")
    private String email;

    @NotNull
    @Schema(description = "пароль пользователя")
    private String password;
}
