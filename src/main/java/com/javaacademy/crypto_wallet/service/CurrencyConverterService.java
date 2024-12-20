package com.javaacademy.crypto_wallet.service;

import java.math.BigDecimal;

public interface CurrencyConverterService {

    BigDecimal getCostInUSD(String currency);
}
