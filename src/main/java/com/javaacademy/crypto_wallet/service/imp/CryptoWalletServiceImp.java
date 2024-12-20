package com.javaacademy.crypto_wallet.service.imp;

import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.dto.UserDto;
import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import com.javaacademy.crypto_wallet.mapper.CryptoWalletMapper;
import com.javaacademy.crypto_wallet.repository.CryptoWalletRepository;
import com.javaacademy.crypto_wallet.service.CryptoWalletService;
import com.javaacademy.crypto_wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptoWalletServiceImp implements CryptoWalletService {

    private final UserService userService;
    private final CryptoWalletRepository cryptoWalletRepository;
    private final CryptoWalletMapper walletMapper;

    @Override
    public CryptoWalletDto getById(UUID id) {
        log.info("Start search wallet with id = {}", id);
        Optional<CryptoWallet> wallet = cryptoWalletRepository.getById(id);
        return wallet.map(walletMapper::convertToCryptoWalletDto)
                .orElseThrow(() -> new RuntimeException("Wallet wit UUID = %s is exist".formatted(id)));
    }

    @Override
    public Stream<CryptoWalletDto> getAllUserWallet(String login) {
        log.info("Start search all user's wallets by login = {}", login);
        userService.findByLogin(login);
        return cryptoWalletRepository.getAllUserWallet(login).map(walletMapper::convertToCryptoWalletDto);
    }

    @Override
    public UUID createWallet(String login, CryptoCurrency currency) {
        log.info("Start creating wallet with currency = {} , userLogin = {}", currency.getDescription(), login);
        UserDto currentUser = userService.findByLogin(login);
        CryptoWallet newWallet = new CryptoWallet(login, currency, BigDecimal.ZERO, UUID.randomUUID());
        cryptoWalletRepository.save(newWallet);
        log.info("Wallet is saved");
        return newWallet.getId();
    }

}
