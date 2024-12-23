package com.javaacademy.crypto_wallet.service.imp;

import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import com.javaacademy.crypto_wallet.exception.EntityNotFoundException;
import com.javaacademy.crypto_wallet.exception.InsufficientFundsException;
import com.javaacademy.crypto_wallet.mapper.CryptoWalletMapper;
import com.javaacademy.crypto_wallet.repository.CryptoWalletRepository;
import com.javaacademy.crypto_wallet.service.CryptoWalletService;
import com.javaacademy.crypto_wallet.service.DollarConverterService;
import com.javaacademy.crypto_wallet.service.RubleConverterService;
import com.javaacademy.crypto_wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptoWalletServiceImp implements CryptoWalletService {

    private final static String TEMPLATE_FOR_CURRENCY_SALE = "Операция прошла успешно. Продано %s %s.";

    private final CryptoWalletMapper walletMapper;
    private final UserService userService;
    private final CryptoWalletRepository cryptoWalletRepository;
    private final DollarConverterService dollarConverterService;
    private final RubleConverterService rubleConverterService;

    @Override
    public CryptoWalletDto getById(UUID id) {
        log.info("Start search wallet with id = {}", id);
        CryptoWallet wallet = getCryptoWalletById(id);
        return walletMapper.convertToCryptoWalletDto(wallet);
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
        userService.findByLogin(login);
        CryptoWallet newWallet = new CryptoWallet(
                UUID.randomUUID(),
                login,
                currency,
                BigDecimal.ZERO
        );
        cryptoWalletRepository.save(newWallet);
        log.info("Wallet is saved");
        return newWallet.getId();
    }

    @Override
    public void refillBalance(UUID walletId, BigDecimal rubleAmount) {
        CryptoWallet wallet = getCryptoWalletById(walletId);
        String currency = wallet.getCurrency().getDescription();
        BigDecimal balance = wallet.getBalance();
        BigDecimal refill = calculateTransactionCostInCurrency(currency, rubleAmount);
        BigDecimal newBalance = balance.add(refill);
        wallet.setBalance(newBalance);
        cryptoWalletRepository.update(wallet);
    }

    @Override
    public String withdrawalBalance(UUID walletId, BigDecimal rubleAmount) {
        CryptoWallet wallet = getCryptoWalletById(walletId);
        String currency = wallet.getCurrency().getDescription();
        BigDecimal balance = wallet.getBalance();
        BigDecimal withdrawal = calculateTransactionCostInCurrency(currency, rubleAmount);
        if (balance.compareTo(withdrawal) < 0) {
            throw new InsufficientFundsException("Not enough money to write off");
        }
        BigDecimal newBalance = balance.subtract(withdrawal);
        wallet.setBalance(newBalance);
        cryptoWalletRepository.update(wallet);
        return TEMPLATE_FOR_CURRENCY_SALE.formatted(withdrawal, currency);
    }

    @Override
    public BigDecimal getBalanceInRubles(UUID walletId) {
        CryptoWallet wallet = getCryptoWalletById(walletId);
        String currency = wallet.getCurrency().getDescription();
        BigDecimal balance = wallet.getBalance();
        return convertWalletBalanceToRuble(currency, balance);
    }

    @Override
    public BigDecimal getBalanceAllWalletsInRuble(String userLogin) {
        userService.findByLogin(userLogin);
        return cryptoWalletRepository.getAllUserWallet(userLogin)
                .map(wallet -> convertWalletBalanceToRuble(wallet.getCurrency().getDescription(),
                        wallet.getBalance())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTransactionCostInCurrency(String currency, BigDecimal rubleAmount) {
        BigDecimal rateInUSD = dollarConverterService.getCostInUSD(currency);
        BigDecimal transactionCostInUSD = rubleConverterService.convertToUSD(rubleAmount);
        return transactionCostInUSD.divide(rateInUSD, 10, RoundingMode.HALF_DOWN);
    }

    private BigDecimal convertWalletBalanceToRuble(String currency, BigDecimal walletBalance) {
        BigDecimal rateInUSD = dollarConverterService.getCostInUSD(currency);
        BigDecimal balanceInUSD = walletBalance.multiply(rateInUSD);
        return rubleConverterService.convertToRUB(balanceInUSD);
    }

    private CryptoWallet getCryptoWalletById(UUID id) {
        return cryptoWalletRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet wit UUID = %s is not exist".formatted(id)));
    }

}
