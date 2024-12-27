package com.javaacademy.crypto_wallet.dto;

import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Представление криптокошелька")
public class CryptoWalletDto {

    @Schema(description = "уникальный номер кошелька")
    private UUID id;
    @Schema(description = "логин пользователя")
    private String ownerLogin;
    @Schema(description = "крюптовалюта")
    private CryptoCurrency currency;
    @Schema(description = "баланс кошелька")
    private BigDecimal balance;

}
