package com.javaacademy.crypto_wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Представление ошибки")
public class ErrorResponseDto {
    @Schema(description = "код ошибки")
    private int code;
    @Schema(description = "описание ошибки")
    private String description;
}
