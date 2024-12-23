package com.javaacademy.crypto_wallet.config;

import com.javaacademy.crypto_wallet.service.DollarConverterService;
import com.javaacademy.crypto_wallet.service.RubleConverterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Profile("local")
@Configuration
public class ConverterServiceLocalConfig {

    @Value("${app.converter.usd.price}")
    private BigDecimal cryptoPriceInUSD;

    @Value("${app.converter.rub.price}")
    private BigDecimal priceUSDinRUB;

    @Bean
    public DollarConverterService converterUSDService() {
        return currency -> cryptoPriceInUSD;
    }

    @Bean
    public RubleConverterService converterRubService() {
        return new RubleConverterService() {
            @Override
            public BigDecimal convertToRUB(BigDecimal quantityUSD) {
                return priceUSDinRUB.multiply(quantityUSD);
            }

            @Override
            public BigDecimal convertToUSD(BigDecimal quantityRUB) {
                return quantityRUB.divide(priceUSDinRUB, 10, RoundingMode.HALF_DOWN);
            }
        };
    }

}
