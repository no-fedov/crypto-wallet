package com.javaacademy.crypto_wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdatePasswordDto {

    @NotNull
    private String login;

    @JsonProperty("old_password")
    @NotNull
    private String oldPassword;

    @JsonProperty("new_password")
    @NotNull
    private String newPassword;

}
