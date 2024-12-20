package com.javaacademy.crypto_wallet.service;

import java.math.BigDecimal;

public interface RubleConverterService {

    BigDecimal convertToRUB(BigDecimal quantityUSD);

    BigDecimal convertToUSD(BigDecimal quantityRUB);
}
