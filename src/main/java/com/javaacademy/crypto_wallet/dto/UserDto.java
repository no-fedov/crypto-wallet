package com.javaacademy.crypto_wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Представление пользователя дял показа")
public class UserDto {

    @Schema(description = "логин пользователя")
    private String login;

}
