package com.javaacademy.crypto_wallet.mapper;

import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import org.springframework.stereotype.Component;

@Component
public class CryptoWalletMapper {

    public CryptoWalletDto convertToCryptoWalletDto(CryptoWallet cryptoWallet) {
        return new CryptoWalletDto(
                cryptoWallet.getId(),
                cryptoWallet.getOwnerLogin(),
                cryptoWallet.getCurrency(),
                cryptoWallet.getBalance()
        );
    }

}
