package com.javaacademy.crypto_wallet.controller;


import com.javaacademy.crypto_wallet.dto.CryptoWalletCreateDto;
import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import com.javaacademy.crypto_wallet.service.CryptoWalletServiceIF;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
public class CryptoWalletController {

    private final CryptoWalletServiceIF cryptoWalletService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createWallet(@RequestBody @Valid CryptoWalletCreateDto dto) {
        String currencyDescription = dto.getCurrency();
        CryptoCurrency currency = CryptoCurrency.getCurrency(currencyDescription);
        return cryptoWalletService.createWallet(dto.getUserLogin(), currency);
    }

    @GetMapping
    public List<CryptoWalletDto> getUserWallets(@RequestParam("username") String userLogin) {
        return cryptoWalletService.getAllUserWallet(userLogin).toList();
    }

}
