package com.javaacademy.crypto_wallet.integration;

import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.entity.CryptoCurrency;
import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.exception.EntityNotFoundException;
import com.javaacademy.crypto_wallet.exception.InsufficientFundsException;
import com.javaacademy.crypto_wallet.service.CryptoWalletService;
import com.javaacademy.crypto_wallet.storage.CryptoWalletStorage;
import com.javaacademy.crypto_wallet.storage.UserStorage;
import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.javaacademy.crypto_wallet.entity.CryptoCurrency.BTC;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("local")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CryptoWalletServiceImpTest {

    private static final String OWNER_LOGIN = "login";
    private static final String OWNER_EMAIL = "email";
    private static final String OWNER_PASSWORD = "password";
    private static final String NOT_EXISTED_USER_LOGIN = "NOT_EXISTED_USER_LOGIN";
    private static final CryptoCurrency CURRENCY = BTC;

    private UUID id;

    @Autowired
    private CryptoWalletService walletService;

    @Autowired
    private CryptoWalletStorage walletStorage;

    @MockBean
    private UserStorage userStorage;

    @BeforeEach
    public void init() {
        id = UUID.randomUUID();
        Mockito.when(userStorage.getByLogin(OWNER_LOGIN))
                .thenReturn(Optional.of(new User(OWNER_LOGIN, OWNER_EMAIL, OWNER_PASSWORD)));
    }

    @Test
    public void getByIdUnsuccessful() {
        UUID id = UUID.randomUUID();
        Assertions.assertThrows(EntityNotFoundException.class, () -> walletService.getById(id));
    }

    @Test
    public void getByIdSuccess() {
        @Cleanup MockedStatic<UUID> uuidMockedStatic = Mockito.mockStatic(UUID.class);
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(id);
        walletService.createWallet(OWNER_LOGIN, CURRENCY);
        CryptoWalletDto expectedWalletDto = new CryptoWalletDto(id, OWNER_LOGIN, CURRENCY, ZERO);
        assertEquals(expectedWalletDto, walletService.getById(id));
    }

    @Test
    public void createWalletSuccess() {
        UUID walletId = walletService.createWallet(OWNER_LOGIN, CURRENCY);
        CryptoWallet expectedWallet = new CryptoWallet(walletId, OWNER_LOGIN, CURRENCY, ZERO);
        CryptoWallet createdWallet = walletStorage.getById(walletId).get();
        assertEquals(expectedWallet, createdWallet);
    }

    @Test
    public void createWalletUnsuccessful() {
        assertThrows(EntityNotFoundException.class, () -> walletService.createWallet(NOT_EXISTED_USER_LOGIN, CURRENCY));
    }

    @Test
    public void getAllUserWalletsSuccess() {
        walletService.createWallet(OWNER_LOGIN, CURRENCY);
        walletService.createWallet(OWNER_LOGIN, CURRENCY);
        Stream<CryptoWalletDto> allUserWallet = walletService.getAllUserWallet(OWNER_LOGIN);
        assertEquals(2, allUserWallet.count());
    }

    @Test
    public void getAllUserWalletUnsuccessful() {
        assertThrows(EntityNotFoundException.class, () -> walletService.getAllUserWallet(NOT_EXISTED_USER_LOGIN));
    }

    @Test
    public void refillBalanceSuccessful() {
        UUID walletId = walletService.createWallet(OWNER_LOGIN, CURRENCY);
        BigDecimal refileRubleAmount = new BigDecimal("1");
        walletService.refillBalance(walletId, refileRubleAmount);
        CryptoWalletDto walletDto = walletService.getById(walletId);
        BigDecimal expectedBalance = new BigDecimal("0.000001");
        BigDecimal balance = walletDto.getBalance();
        System.out.println(balance);
        assertEquals(0, expectedBalance.compareTo(balance));
    }

    @Test
    public void refillBalanceUnsuccessful() {
        assertThrows(EntityNotFoundException.class, () -> walletService.refillBalance(id, new BigDecimal("1000")));
    }

    @Test
    public void withdrawalBalanceSuccess() {
        BigDecimal currentBalanceBTC = new BigDecimal("2000");
        CryptoWallet wallet = new CryptoWallet(id, OWNER_LOGIN, CURRENCY, currentBalanceBTC);
        walletStorage.save(wallet);
        BigDecimal withdrawalRubleAmount = new BigDecimal("1");
        String transactionDescription = walletService.withdrawalBalance(id, withdrawalRubleAmount);
        System.out.println(transactionDescription);
        CryptoWallet walletWithUpdatedBalance = walletStorage.getById(id).get();
        BigDecimal expectedBalance = new BigDecimal("1999.999999");
        BigDecimal currentBalance = walletWithUpdatedBalance.getBalance();
        assertEquals(0, expectedBalance.compareTo(currentBalance));
    }

    @Test
    public void withdrawalBalanceUnsuccessfulNotFoundWallet() {
        assertThrows(EntityNotFoundException.class, () -> walletService.withdrawalBalance(id, BigDecimal.TEN));
    }

    @Test
    public void withdrawalBalanceUnsuccessfulInsufficientFunds() {
        CryptoWallet wallet = new CryptoWallet(id, OWNER_LOGIN, CURRENCY, ZERO);
        walletStorage.save(wallet);
        assertThrows(InsufficientFundsException.class, () -> walletService.withdrawalBalance(id, BigDecimal.TEN));
    }

    @Test
    public void getBalanceInRublesSuccess() {
        CryptoWallet wallet = new CryptoWallet(id, OWNER_LOGIN, BTC, new BigDecimal("0.000001"));
        walletStorage.save(wallet);
        BigDecimal currentBalance = walletService.getBalanceInRubles(id);
        BigDecimal expectedBalance = new BigDecimal("1");
        assertEquals(0,  expectedBalance.compareTo(currentBalance));
    }

    @Test
    public void getBalanceInRublesUnsuccessful() {
        assertThrows(EntityNotFoundException.class, () -> walletService.getBalanceInRubles(id));
    }

    @Test
    public void getBalanceAllWalletForNotExistentUserUnsuccessful() {
        assertThrows(EntityNotFoundException.class,
                () -> walletService.getBalanceAllWalletsInRuble(NOT_EXISTED_USER_LOGIN));
    }

    @Test
    public void getBalanceAllWalletsInRubles() {
        CryptoWallet wallet1 = new CryptoWallet(UUID.randomUUID(), OWNER_LOGIN, BTC, new BigDecimal("0.00001"));
        CryptoWallet wallet2 = new CryptoWallet(UUID.randomUUID(), OWNER_LOGIN, BTC, new BigDecimal("0.000010001"));
        walletStorage.save(wallet1);
        walletStorage.save(wallet2);
        BigDecimal currentAmountBalance = walletService.getBalanceAllWalletsInRuble(OWNER_LOGIN);
        BigDecimal expectedBalance = new BigDecimal("20.001");
        assertEquals(0, expectedBalance.compareTo(currentAmountBalance));
    }
}
