package com.javaacademy.crypto_wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoWalletCreateDto {

    @NotNull
    @JsonProperty("username")
    private String userLogin;

    @NotNull
    @JsonProperty("crypto_type")
    private String currency;

}
