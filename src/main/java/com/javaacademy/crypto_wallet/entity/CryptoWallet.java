package com.javaacademy.crypto_wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CryptoWallet {

    private final UUID id;
    private final String ownerLogin;
    private final CryptoCurrency currency;
    private BigDecimal balance;

}
