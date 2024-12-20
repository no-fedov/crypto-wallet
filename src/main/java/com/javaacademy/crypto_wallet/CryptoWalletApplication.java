package com.javaacademy.crypto_wallet;

import com.javaacademy.crypto_wallet.service.CurrencyConverterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CryptoWalletApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CryptoWalletApplication.class, args);
        CurrencyConverterService bean = context.getBean(CurrencyConverterService.class);
        System.out.println(bean.getCostInUSD("bitcoin"));
    }

}
