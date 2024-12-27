package com.javaacademy.crypto_wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Представление для создания криптокошелька")
public class CryptoWalletCreateDto {

    @NotNull
    @JsonProperty("username")
    @Schema(description = "логин пользователя",
            example = "den4ik")
    private String userLogin;

    @NotNull
    @JsonProperty("crypto_type")
    @Schema(description = "криптовалюта",
            example = "bitcoin")
    private String currency;

}
