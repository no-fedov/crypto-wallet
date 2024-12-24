package com.javaacademy.crypto_wallet.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.services.currency-converter")
public class DollarServiceProperty {

    private String url;
    private String header;
    private String token;

}
