package com.javaacademy.crypto_wallet.integration;

import com.javaacademy.crypto_wallet.service.DollarConverterService;
import com.javaacademy.crypto_wallet.service.RubleConverterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("local")
public class ConverterServicesLocalConfigTest {

    @Autowired
    private DollarConverterService dollarConverterService;

    @Autowired
    private RubleConverterService rubleConverterService;

    @Test
    public void convertCurrencyToDollar() {
        BigDecimal costInUSD = dollarConverterService.getCostInUSD("BTC");
        assertEquals(0, costInUSD.compareTo(new BigDecimal("10000")));
    }

    @Test
    public void convertDollarToRuble() {
        BigDecimal rubles = rubleConverterService.convertToRUB(new BigDecimal("1"));
        assertEquals(0, rubles.compareTo(new BigDecimal("100")));
    }

    @Test
    public void convertRubleToDollar() {
        BigDecimal dollars = rubleConverterService.convertToUSD(new BigDecimal("100"));
        assertEquals(0, dollars.compareTo(new BigDecimal("1")));
    }
}
