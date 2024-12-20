package com.javaacademy.crypto_wallet.config;

import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CurrencyServiceProperty.class, RubleServiceProperty.class})
public class AppConfig {

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}
