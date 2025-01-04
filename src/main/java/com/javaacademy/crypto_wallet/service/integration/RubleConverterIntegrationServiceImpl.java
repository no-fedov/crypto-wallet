package com.javaacademy.crypto_wallet.service.integration;

import com.javaacademy.crypto_wallet.config.RubleServiceProperty;
import com.javaacademy.crypto_wallet.service.RubleConverterService;
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
import java.math.RoundingMode;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class RubleConverterIntegrationServiceImpl implements RubleConverterService {

    private static final String JSON_PATH_CONVERTER_USD = "$['rates'].['USD']";
    private static final int SCALE_FOR_ROUND_OPERATION = 10;

    private final OkHttpClient client;
    private final RubleServiceProperty property;

    @Override
    public BigDecimal convertToRUB(BigDecimal quantityUSD) {
        return quantityUSD.divide(getPriceUSD(), SCALE_FOR_ROUND_OPERATION, RoundingMode.DOWN);
    }

    @Override
    public BigDecimal convertToUSD(BigDecimal quantityRUB) {
        return quantityRUB.multiply(getPriceUSD());
    }

    @SneakyThrows
    private BigDecimal getPriceUSD() {
        @Cleanup
        Response response = client.newCall(constructRequest()).execute();
        if (response.isSuccessful() && response.body() != null) {
            String responseBody = response.body().string();

            if (!responseBody.isBlank()) {
                return JsonPath.parse(responseBody).
                        read(JsonPath.compile(JSON_PATH_CONVERTER_USD),
                                BigDecimal.class
                        );
            }
        }
        throw new RuntimeException("Неудачный запрос");
    }

    private Request constructRequest() {
        return new Request.Builder()
                .url(property.getUrl())
                .get()
                .build();
    }
}
