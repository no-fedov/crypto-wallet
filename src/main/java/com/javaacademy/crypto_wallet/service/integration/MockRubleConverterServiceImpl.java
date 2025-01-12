package com.javaacademy.crypto_wallet.service.integration;

import com.javaacademy.crypto_wallet.service.RubleConverterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Profile("local")
public class MockRubleConverterServiceImpl implements RubleConverterService {

    private static final int SCALE_FOR_ROUND_OPERATION = 10;

    @Value("${app.converter.usd.price}")
    private BigDecimal priceUSDinRUB;

    @Override
    public BigDecimal convertToRUB(BigDecimal quantityUSD) {
        return priceUSDinRUB.multiply(quantityUSD);
    }

    @Override
    public BigDecimal convertToUSD(BigDecimal quantityRUB) {
        return quantityRUB.divide(priceUSDinRUB, SCALE_FOR_ROUND_OPERATION, RoundingMode.HALF_DOWN);
    }
}
