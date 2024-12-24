package com.javaacademy.crypto_wallet.service.integration;

import com.javaacademy.crypto_wallet.config.DollarServiceProperty;
import com.javaacademy.crypto_wallet.service.DollarConverterService;
import com.jayway.jsonpath.JsonPath;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class DollarConverterIntegrationServiceImpl implements DollarConverterService {

    private final OkHttpClient client;
    private final DollarServiceProperty property;
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
            return JsonPath.parse(responseBody).
                    read(JsonPath.compile(JSON_PATH_CONVERTER_TO_USD_TEMPLATE.formatted(currency)),
                            BigDecimal.class
                    );
            }
        }
        throw new RuntimeException("Неудачный запрос");
    }

    private Request constructRequest(String url) {
        return new Request.Builder()
                .get()
                .addHeader(property.getHeader(), property.getToken())
                .url(constructUrl(url))
                .build();
    }

    private String constructUrl(String currencyParameterURL) {
        return property.getUrl() + URL_TEMPLATE.formatted(currencyParameterURL);
    }
}
