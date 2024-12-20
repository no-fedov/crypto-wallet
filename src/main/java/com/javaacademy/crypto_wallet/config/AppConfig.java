package com.javaacademy.crypto_wallet.config;

import com.javaacademy.crypto_wallet.service.CurrencyConverterService;
import com.jayway.jsonpath.JsonPath;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(CurrencyServiceProperty.class)
public class AppConfig {

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    @Profile("local")
    public CurrencyConverterService currencyConverterService() {

    }
}
