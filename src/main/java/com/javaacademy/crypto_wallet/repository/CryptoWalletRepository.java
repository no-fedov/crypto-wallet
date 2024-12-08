package com.javaacademy.crypto_wallet.repository;

import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import com.javaacademy.crypto_wallet.storage.CryptoWalletStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CryptoWalletRepository {

    private final CryptoWalletStorage storage;

    public void save(CryptoWallet cryptoWallet) {
        storage.save(cryptoWallet);
    }

    public Optional<CryptoWallet> getById(UUID id) {
        return storage.getById(id);
    }

    public Stream<CryptoWallet> getAllUserWallet(String login) {
        return storage.getAllUserWallet(login);
    }

}
