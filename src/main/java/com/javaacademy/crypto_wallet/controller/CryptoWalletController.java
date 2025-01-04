package com.javaacademy.crypto_wallet.controller;


import com.javaacademy.crypto_wallet.dto.CryptoWalletCreateDto;
import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.dto.CryptoWalletTransactionDto;
import com.javaacademy.crypto_wallet.dto.ErrorResponseDto;
import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import com.javaacademy.crypto_wallet.service.CryptoWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
@Tag(name = "Контроллер для работы с криптокошельками",
        description = "Содержит логику, предоставленную пользователю, для работы с криптокошельками")
public class CryptoWalletController {

    private final CryptoWalletService cryptoWalletService;

    @Operation(
            summary = "Создает криптокошелек",
            description = "Сохраняет критокошелек для существующего пользователя и возращает номер кошелька")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Криптокошелек создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UUID.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найден пользователь или валюта, для которых создается кошелек",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createWallet(@RequestBody @Valid CryptoWalletCreateDto dto) {
        log.info("POST /cryptowallet wallet = {}", dto);
        String currencyDescription = dto.getCurrency();
        CryptoCurrency currency = CryptoCurrency.getCurrency(currencyDescription);
        UUID walletId = cryptoWalletService.createWallet(dto.getUserLogin(), currency);
        log.info("Create wallet with id = {}", walletId);
        return walletId;
    }

    @Operation(
            summary = "Возвращет пользователю его криптокошельки",
            description = "Пользователю выводится список кошельков с их номером, валютой и балансом")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Найдены криптокошельки",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = CryptoWalletDto.class
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найден пользователь",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping
    public List<CryptoWalletDto> getUserWallets(@RequestParam("username") String userLogin) {
        log.info("GET /cryptowallet?username={}", userLogin);
        List<CryptoWalletDto> allUserWallet = cryptoWalletService.getAllUserWallet(userLogin).toList();
        log.info("Found wallets: {}", allUserWallet);
        return allUserWallet;
    }

    @Operation(
            summary = "Пополняет баланс кошелька на указанную сумму",
            description = "Пользователь передает номер кошелька и сумму пополнения в рублях")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Баланс кошелька пополнен"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найден кошелек",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/refill")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refillRubBalance(@RequestBody CryptoWalletTransactionDto dto) {
        cryptoWalletService.refillBalance(dto.getId(), dto.getRubAmount());
    }

    @Operation(
            summary = "Списывает с баланса кошелька указанную сумму",
            description = "Пользователь передает номер кошелька и сумму списания в рублях")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Деньги списаны",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найден кошелек",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping(value = "/withdrawal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> withdrawRubBalance(@RequestBody CryptoWalletTransactionDto dto) {
        String response = cryptoWalletService.withdrawalBalance(dto.getId(), dto.getRubAmount());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(
            summary = "Узнать баланса кошелька в рублях",
            description = "Возвращает баланс криптокошелька в рублях")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Баланс в рублях",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найден кошелек",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/balance/{id}")
    public BigDecimal getRubBalanceWallet(@PathVariable("id")
                                       @Parameter(description = "Уникальный номер кошелька в формате UUID")
                                       String walletId) {
        return cryptoWalletService.getBalanceInRubles(UUID.fromString(walletId))
                .setScale(2, RoundingMode.DOWN);
    }

    @Operation(
            summary = "Узнать сколько денег у пользователя в рублях",
            description = "Возвращает общий баланс со всех криптокошелька в рублях")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Баланс в рублях",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найден пользователь",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/balance")
    public BigDecimal getRubBalanceWallets(@RequestParam("username") String userLogin) {
        return cryptoWalletService.getBalanceAllWalletsInRuble(userLogin)
                .setScale(2, RoundingMode.DOWN);
    }

}
