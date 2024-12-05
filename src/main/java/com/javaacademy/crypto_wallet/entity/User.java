package com.javaacademy.crypto_wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class User {

    @NonNull
    private final String login;

    @NonNull
    private String email;

    @NonNull
    private String password;

}
