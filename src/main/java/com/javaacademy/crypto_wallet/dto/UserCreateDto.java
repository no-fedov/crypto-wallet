package com.javaacademy.crypto_wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateDto {

    private String login;
    private String email;
    private String password;

}
