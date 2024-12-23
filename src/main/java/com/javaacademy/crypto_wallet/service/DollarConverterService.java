package com.javaacademy.crypto_wallet.service;

import java.math.BigDecimal;

public interface DollarConverterService {

    BigDecimal getCostInUSD(String currency);
}
