package com.javaacademy.crypto_wallet.dto;

import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CryptoWalletDto {

    private String ownerLogin;
    private CryptoCurrency currency;
    private BigDecimal amountCurrency;
    private UUID id;

}
