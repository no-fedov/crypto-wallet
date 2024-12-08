package com.javaacademy.crypto_wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum CryptoCurrency {

    BTC("bitcoin"),
    ETH("ethereum"),
    SOL("solana");

    private final String description;

    public static CryptoCurrency getCurrency(String currency) {
        Map<String, CryptoCurrency> currencyMap = Arrays.stream(values())
                .collect(Collectors.toMap(CryptoCurrency::getDescription, e -> e));

        if (currencyMap.containsKey(currency)) {
            return currencyMap.get(currency);
        }

        throw new RuntimeException("Currency '%s' is exist".formatted(currency));
    }
}
