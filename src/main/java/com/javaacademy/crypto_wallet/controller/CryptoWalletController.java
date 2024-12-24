package com.javaacademy.crypto_wallet.controller;


import com.javaacademy.crypto_wallet.dto.CryptoWalletTransactionDto;
import com.javaacademy.crypto_wallet.dto.CryptoWalletCreateDto;
import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import com.javaacademy.crypto_wallet.service.CryptoWalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
public class CryptoWalletController {

    private final CryptoWalletService cryptoWalletService;

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

    @GetMapping
    public List<CryptoWalletDto> getUserWallets(@RequestParam("username") String userLogin) {
        log.info("GET /cryptowallet?username={}", userLogin);
        List<CryptoWalletDto> allUserWallet = cryptoWalletService.getAllUserWallet(userLogin).toList();
        log.info("Found wallets: {}", allUserWallet);
        return allUserWallet;
    }

    @PostMapping("/refill")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refillRubBalance(@RequestBody CryptoWalletTransactionDto dto) {
        cryptoWalletService.refillBalance(dto.getId(), dto.getRubAmount());
    }

    @PostMapping("/withdrawal")
    @ResponseStatus(HttpStatus.CREATED)
    public String withdrawRubBalance(@RequestBody CryptoWalletTransactionDto dto) {
       String requestBody = cryptoWalletService.withdrawalBalance(dto.getId(), dto.getRubAmount());
       return requestBody;
    }

    @GetMapping("/balance/{id}")
    public BigDecimal getUserRubWallet(@PathVariable("id") String walletId) {
        return cryptoWalletService.getBalanceInRubles(UUID.fromString(walletId))
                .setScale(2, RoundingMode.DOWN);
    }

    @GetMapping("/balance")
    public BigDecimal getUserRubWallets(@RequestParam("username") String userLogin) {
        return cryptoWalletService.getBalanceAllWalletsInRuble(userLogin)
                .setScale(2, RoundingMode.DOWN);
    }

}
