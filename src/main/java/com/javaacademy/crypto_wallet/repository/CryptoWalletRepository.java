package com.javaacademy.crypto_wallet.repository;

import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import com.javaacademy.crypto_wallet.storage.CryptoWalletStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptoWalletRepository {

    private final CryptoWalletStorage storage;

    public void save(CryptoWallet cryptoWallet) {
        storage.save(cryptoWallet);
        log.info("Wallet with id = {} is saved", cryptoWallet.getId());
    }

    public Optional<CryptoWallet> getById(UUID id) {
        log.info("Search wallet where id = {}", id);
        return storage.getById(id);
    }

    public Stream<CryptoWallet> getAllUserWallet(String login) {
        log.info("Search wallet where username = {}", login);
        return storage.getAllUserWallet(login);
    }

}
