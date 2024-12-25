package com.javaacademy.crypto_wallet.service.integration;

import com.javaacademy.crypto_wallet.service.DollarConverterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("local")
public class MockDollarConverterServiceImpl implements DollarConverterService {

    @Value("${app.converter.crypto.price}")
    private BigDecimal cryptoPriceInUSD;

    @Override
    public BigDecimal getCostInUSD(String currency) {
        return cryptoPriceInUSD;
    }
}
