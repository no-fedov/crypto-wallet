package com.javaacademy.crypto_wallet.service;

import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.entity.CryptoCurrency;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

public interface CryptoWalletService {

    CryptoWalletDto getById(UUID id);

    Stream<CryptoWalletDto> getAllUserWallet(String login);

    UUID createWallet(String login, CryptoCurrency currency);

    void refillBalance(UUID walletId, BigDecimal rubleAmount);

    String withdrawalBalance(UUID walletId, BigDecimal rubleAmount);

    BigDecimal getBalanceInRubles(UUID walletId);

    BigDecimal getBalanceAllWalletsInRuble(String userLogin);
}
