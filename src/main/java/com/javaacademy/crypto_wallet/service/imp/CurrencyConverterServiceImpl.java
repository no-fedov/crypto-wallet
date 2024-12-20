package com.javaacademy.crypto_wallet.service.imp;

import com.javaacademy.crypto_wallet.config.CurrencyServiceProperty;
import com.javaacademy.crypto_wallet.service.CurrencyConverterService;
import com.jayway.jsonpath.JsonPath;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@Profile("prod")
@RequiredArgsConstructor
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private final OkHttpClient client;
    private final CurrencyServiceProperty property;
    private static final String URL_TEMPLATE = "/simple/price?ids=%s&vs_currencies=usd";
    private static final String JSON_PATH_CONVERTER_TO_USD_TEMPLATE = "$['%s']['usd']";

    @Override
    @SneakyThrows
    public BigDecimal getCostInUSD(String currency) {
        @Cleanup
        Response response = client.newCall(constructRequest(currency)).execute();
        if (response.isSuccessful() && response.body() != null) {
            String responseBody = response.body().string();

            if (!responseBody.isBlank()) {
            log.info(JSON_PATH_CONVERTER_TO_USD_TEMPLATE.formatted(currency));
            return JsonPath.parse(responseBody).
                    read(JsonPath.compile(JSON_PATH_CONVERTER_TO_USD_TEMPLATE.formatted(currency)),
                            BigDecimal.class
                    );
            }
        }
        throw new RuntimeException("Неудачный запрос");
    }

    private Request constructRequest(String currencyRequestParameter) {
        return new Request.Builder()
                .get()
                .addHeader(property.getHeader(), property.getToken())
                .url(constructUrl(currencyRequestParameter))
                .build();
    }

    private String constructUrl(String currency) {
        return property.getUrl() + URL_TEMPLATE.formatted(currency);
    }
}
