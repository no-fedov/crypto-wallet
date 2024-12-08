package com.javaacademy.crypto_wallet.service;

import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.dto.UserDto;
import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import com.javaacademy.crypto_wallet.mapper.CryptoWalletMapper;
import com.javaacademy.crypto_wallet.repository.CryptoWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CryptoWalletService implements CryptoWalletServiceIF {

    private final UserServiceIF userService;
    private final CryptoWalletRepository cryptoWalletRepository;
    private final CryptoWalletMapper walletMapper;

    @Override
    public CryptoWalletDto getById(UUID id) {
        Optional<CryptoWallet> wallet = cryptoWalletRepository.getById(id);
        return wallet.map(walletMapper::convertToCryptoWalletDto)
                .orElseThrow(() -> new RuntimeException("Wallet wit UUID = %s is exist".formatted(id)));
    }

    @Override
    public Stream<CryptoWalletDto> getAllUserWallet(String login) {
        userService.findByLogin(login);
        return cryptoWalletRepository.getAllUserWallet(login).map(walletMapper::convertToCryptoWalletDto);
    }

    @Override
    public UUID createWallet(String login, CryptoCurrency currency) {
        UserDto currentUser = userService.findByLogin(login);
        CryptoWallet newWallet = new CryptoWallet(login, currency, BigDecimal.ZERO, UUID.randomUUID());
        cryptoWalletRepository.save(newWallet);
        return newWallet.getId();
    }

}
