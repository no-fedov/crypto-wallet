package com.javaacademy.crypto_wallet;

import com.javaacademy.crypto_wallet.service.CurrencyConverterService;
import com.javaacademy.crypto_wallet.service.RubleConverterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;

@SpringBootApplication
public class CryptoWalletApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CryptoWalletApplication.class, args);
        CurrencyConverterService bean = context.getBean(CurrencyConverterService.class);
//        System.out.println(bean.getCostInUSD("bitcoin"));
        RubleConverterService bean2 = context.getBean(RubleConverterService.class);
        System.out.println("100 рублей стоят = " + bean2.convertToUSD(BigDecimal.valueOf(100)) + " долларов");
        System.out.println("1 доллар стоит = " + bean2.convertToRUB(BigDecimal.valueOf(1)) + " рублей");
    }

}
