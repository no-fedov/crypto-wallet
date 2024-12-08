package com.javaacademy.crypto_wallet.storage;

import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class CryptoWalletStorage {

    private final Map<UUID, CryptoWallet> storage = new HashMap<>();

    public void save(CryptoWallet cryptoWallet) {
        UUID id = cryptoWallet.getId();
        if (storage.containsKey(id)) {
            throw new RuntimeException("CryptoWallet with id = '%s' already exists".formatted(id));
        }
        storage.put(id, cryptoWallet);
    }

    public Optional<CryptoWallet> getById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Stream<CryptoWallet> getAllUserWallet(String login) {
        return storage.values().stream().filter(cryptoWallet -> Objects.equals(cryptoWallet.getOwnerLogin(), login));
    }
}
