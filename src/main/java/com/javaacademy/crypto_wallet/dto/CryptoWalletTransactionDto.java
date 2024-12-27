package com.javaacademy.crypto_wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Операция по изменениею баланса кошелька")
public class CryptoWalletTransactionDto {

    @Schema(description = "уникальный номер кошелька")
    @JsonProperty("account_id")
    private UUID id;

    @Schema(description = "сумма операции в рублях")
    @JsonProperty("rubles_amount")
    private BigDecimal rubAmount;

}
