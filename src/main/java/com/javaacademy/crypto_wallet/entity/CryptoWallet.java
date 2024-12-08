package com.javaacademy.crypto_wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CryptoWallet {

    private String ownerLogin;
    private CryptoCurrency currency;
    private BigDecimal amountCurrency;
    private UUID id;

}
