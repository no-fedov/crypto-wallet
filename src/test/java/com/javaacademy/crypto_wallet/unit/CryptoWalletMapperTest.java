package com.javaacademy.crypto_wallet.unit;

import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import com.javaacademy.crypto_wallet.mapper.CryptoWalletMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.javaacademy.crypto_wallet.entity.CryptoCurrency.BTC;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CryptoWalletMapperTest {

    private static final String LOGIN_OWNER = "login";

    @Test
    public void convertToDto() {
        CryptoWalletMapper walletMapper = new CryptoWalletMapper();
        UUID walletId = UUID.randomUUID();
        CryptoWallet wallet = new CryptoWallet(
                walletId,
                LOGIN_OWNER,
                BTC,
                ZERO
        );
        CryptoWalletDto expectedWalletDto = new CryptoWalletDto(
                walletId,
                LOGIN_OWNER,
                BTC,
                ZERO
        );
        CryptoWalletDto walletDto = walletMapper.convertToCryptoWalletDto(wallet);
        assertEquals(expectedWalletDto, walletDto);
    }
}
