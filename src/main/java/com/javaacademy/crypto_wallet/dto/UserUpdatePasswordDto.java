package com.javaacademy.crypto_wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Представление для обновления пользователя")
public class UserUpdatePasswordDto {

    @NotNull
    @Schema(description = "логин пользователя")
    private String login;

    @JsonProperty("old_password")
    @NotNull
    @Schema(description = "старый пароль пользователя")
    private String oldPassword;

    @JsonProperty("new_password")
    @NotNull
    @Schema(description = "новый пароль пользователя")
    private String newPassword;

}
