package com.javaacademy.crypto_wallet.unit;

import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import com.javaacademy.crypto_wallet.exception.EntityAlreadyExistException;
import com.javaacademy.crypto_wallet.exception.EntityNotFoundException;
import com.javaacademy.crypto_wallet.storage.CryptoWalletStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.javaacademy.crypto_wallet.entity.CryptoCurrency.BTC;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CryptoWalletStorageTest {

    private static final String OWNER_WALLET_LOGIN = "Олег";

    private UUID walletId;
    private final CryptoCurrency walletCurrency = BTC;
    private CryptoWallet wallet;

    @BeforeEach
    public void init() {
        walletId = UUID.randomUUID();
        wallet = new CryptoWallet(
                walletId,
                OWNER_WALLET_LOGIN,
                walletCurrency,
                ZERO
        );
    }

    @Test
    public void saveWalletSuccess() {
        CryptoWalletStorage walletStorage = new CryptoWalletStorage();
        assertDoesNotThrow(() -> walletStorage.save(wallet));
    }

    @Test
    public void saveWalletWitDuplicateUUIDUnsuccessful() {
        CryptoWalletStorage walletStorage = new CryptoWalletStorage();
        walletStorage.save(wallet);
        assertThrows(EntityAlreadyExistException.class, () -> walletStorage.save(wallet));
    }

    @Test
    public void getWalletByIdReturnNull() {
        CryptoWalletStorage cryptoWalletStorage = new CryptoWalletStorage();
        Optional<CryptoWallet> walletOptional = cryptoWalletStorage.getById(walletId);
        assertTrue(walletOptional.isEmpty());
    }

    @Test
    public void getWalletByIdSuccess() {
        CryptoWalletStorage walletStorage = new CryptoWalletStorage();
        walletStorage.save(wallet);
        Optional<CryptoWallet> walletOptional = walletStorage.getById(walletId);
        assertEquals(wallet, walletOptional.get());
    }

    @Test
    public void getAllUserWalletSuccess() {
        CryptoWalletStorage walletStorage = new CryptoWalletStorage();
        walletStorage.save(wallet);
        CryptoWallet newWallet = new CryptoWallet(
                UUID.randomUUID(),
                OWNER_WALLET_LOGIN,
                BTC,
                ZERO
        );
        walletStorage.save(newWallet);
        Stream<CryptoWallet> allUserWallet = walletStorage.getAllUserWallet(OWNER_WALLET_LOGIN);
        assertEquals(List.of(wallet, newWallet).size(), allUserWallet.count());
    }

    @Test
    public void updateWalletBalanceSuccessful() {
        CryptoWalletStorage walletStorage = new CryptoWalletStorage();
        walletStorage.save(wallet);
        BigDecimal newBalance = new BigDecimal("1000");
        CryptoWallet updatedWallet = new CryptoWallet(
                walletId,
                OWNER_WALLET_LOGIN,
                walletCurrency,
                newBalance
        );
        walletStorage.update(updatedWallet);
        Optional<CryptoWallet> walletOptional = walletStorage.getById(walletId);
        assertEquals(newBalance, walletOptional.get().getBalance());
        assertEquals(updatedWallet, walletStorage.getById(walletId).get());
    }

    @Test
    public void updateNotExistWalletUnsuccessful() {
        CryptoWalletStorage walletStorage = new CryptoWalletStorage();
        assertThrows(EntityNotFoundException.class, () -> walletStorage.update(wallet));
    }
}
