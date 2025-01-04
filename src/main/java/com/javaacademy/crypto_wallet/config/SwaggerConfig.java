package com.javaacademy.crypto_wallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        Info info = new Info()
                .title("Это api приложения \"Криптокошелек\"")
                .description("Приложение позволяет регестрировать пользователей и создавать криптокошельки."
                        + " Пополнять и списывать баланс кошельков, а также узнавать их баланс в рублях");
        return new OpenAPI().info(info);
    }

}
