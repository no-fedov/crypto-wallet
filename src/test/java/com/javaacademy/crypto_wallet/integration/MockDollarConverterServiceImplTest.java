package com.javaacademy.crypto_wallet.integration;

import com.javaacademy.crypto_wallet.service.DollarConverterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("local")
public class MockDollarConverterServiceImplTest {

    @Autowired
    private DollarConverterService dollarConverterService;

    @Test
    public void convertCurrencyToDollar() {
        BigDecimal costInUSD = dollarConverterService.getCostInUSD("BTC");
        assertEquals(0, costInUSD.compareTo(new BigDecimal("10000")));
    }
}
