package com.javaacademy.crypto_wallet.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateDto {

    @NotNull
    private String login;

    @NotNull
    private String email;

    @NotNull
    private String password;

}
