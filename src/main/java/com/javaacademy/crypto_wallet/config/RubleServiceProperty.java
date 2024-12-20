package com.javaacademy.crypto_wallet.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.services.usd-converter")
public class RubleServiceProperty {

    private String url;

}
