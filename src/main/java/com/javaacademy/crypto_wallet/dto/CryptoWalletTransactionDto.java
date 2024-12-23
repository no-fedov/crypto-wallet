package com.javaacademy.crypto_wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoWalletTransactionDto {

    @JsonProperty("account_id")
    private UUID id;

    @JsonProperty("rubles_amount")
    private BigDecimal rubAmount;

}
