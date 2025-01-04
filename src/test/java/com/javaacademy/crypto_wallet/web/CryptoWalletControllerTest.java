package com.javaacademy.crypto_wallet.web;

import com.javaacademy.crypto_wallet.dto.CryptoWalletCreateDto;
import com.javaacademy.crypto_wallet.dto.CryptoWalletDto;
import com.javaacademy.crypto_wallet.dto.CryptoWalletTransactionDto;
import com.javaacademy.crypto_wallet.entity.CryptoWallet;
import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.storage.CryptoWalletStorage;
import com.javaacademy.crypto_wallet.storage.UserStorage;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static com.javaacademy.crypto_wallet.entity.CryptoCurrency.BTC;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("local")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CryptoWalletControllerTest {

    @Value("${server.port}")
    private int port;

    private static final String USER_LOGIN = "den4ik";
    private static final String USER_EMAIL = "email@mail.ru";
    private static final String USER_PASSWORD = "111111";
    private static final String NOT_EXIST_USER_LOGIN = "RANDOM_LOGIN";
    private static final String NOT_EXIST_CURRENCY = "RANDOM_CURRENCY";

    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private CryptoWalletStorage walletStorage;

    @BeforeEach
    public void init() {
        userStorage.save(new User(USER_LOGIN, USER_EMAIL, USER_PASSWORD));
    }

    @PostConstruct
    public void setSpecification() {
        requestSpecification = new RequestSpecBuilder()
                .setPort(port)
                .setBasePath("/cryptowallet")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void createWalletSuccess() {
        CryptoWalletCreateDto walletCreateDto = new CryptoWalletCreateDto(USER_LOGIN, BTC.getDescription());
        UUID newWalletId = given(requestSpecification)
                .body(walletCreateDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        CryptoWallet wallet = walletStorage.getById(newWalletId).get();
        assertEquals(walletCreateDto.getUserLogin(), wallet.getOwnerLogin());
        assertEquals(walletCreateDto.getCurrency(), wallet.getCurrency().getDescription());
        assertEquals(0, wallet.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void createWalletForNotExistUser() {
        CryptoWalletCreateDto walletCreateDto = new CryptoWalletCreateDto(NOT_EXIST_USER_LOGIN, BTC.getDescription());
        given(requestSpecification)
                .body(walletCreateDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.NOT_FOUND.value());
        Stream<CryptoWallet> allUserWallet = walletStorage.getAllUserWallet(NOT_EXIST_USER_LOGIN);
        assertTrue(allUserWallet.findAny().isEmpty());
    }

    @Test
    public void createWalletForNotExistCurrency() {
        CryptoWalletCreateDto walletCreateDto = new CryptoWalletCreateDto(USER_LOGIN, NOT_EXIST_CURRENCY);
        given(requestSpecification)
                .body(walletCreateDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.NOT_FOUND.value());
        Stream<CryptoWallet> allUserWallet = walletStorage.getAllUserWallet(USER_LOGIN);
        assertTrue(allUserWallet.findAny().isEmpty());
    }

    @Test
    public void getAllUserWallet() {
        walletStorage.save(new CryptoWallet(UUID.randomUUID(), USER_LOGIN, BTC, BigDecimal.ZERO));
        walletStorage.save(new CryptoWallet(UUID.randomUUID(), USER_LOGIN, BTC, BigDecimal.ZERO));
        List<CryptoWalletDto> cryptoWallets = given(requestSpecification)
                .queryParams(Map.of("username", USER_LOGIN))
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        assertEquals(2, cryptoWallets.size());
    }

    @Test
    public void refillBalanceSuccess() {
        UUID newWalletId = UUID.randomUUID();
        BigDecimal rubleAmountRefill = new BigDecimal("10000");
        walletStorage.save(new CryptoWallet(newWalletId, USER_LOGIN, BTC, BigDecimal.ZERO));
        CryptoWalletTransactionDto transactionDto = new CryptoWalletTransactionDto(newWalletId, rubleAmountRefill);
        given(requestSpecification)
                .body(transactionDto)
                .post("/refill")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.NO_CONTENT.value());
        BigDecimal balanceInBTC = walletStorage.getById(newWalletId).get().getBalance();
        assertEquals(0, balanceInBTC.compareTo(new BigDecimal("0.01")));
    }

    @Test
    public void refillBalanceNotExistWallet() {
        UUID newWalletId = UUID.randomUUID();
        BigDecimal rubleAmountRefill = new BigDecimal("10000");
        CryptoWalletTransactionDto transactionDto = new CryptoWalletTransactionDto(newWalletId, rubleAmountRefill);
        given(requestSpecification)
                .body(transactionDto)
                .post("/refill")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.NOT_FOUND.value());
        assertTrue(walletStorage.getById(newWalletId).isEmpty());
    }

    @Test
    public void withdrawRubBalanceNotExistWallet() {
        UUID newWalletId = UUID.randomUUID();
        BigDecimal rubleAmountRefill = new BigDecimal("10000");
        CryptoWalletTransactionDto transactionDto = new CryptoWalletTransactionDto(newWalletId, rubleAmountRefill);
        given(requestSpecification)
                .body(transactionDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.NOT_FOUND.value());
        assertTrue(walletStorage.getById(newWalletId).isEmpty());
    }

    @Test
    public void withdrawRubBalanceSuccessful() {
        UUID newWalletId = UUID.randomUUID();
        walletStorage.save(new CryptoWallet(newWalletId, USER_LOGIN, BTC, BigDecimal.ONE));
        BigDecimal rubleAmountWithdraw = new BigDecimal("10000");
        CryptoWalletTransactionDto transactionDto = new CryptoWalletTransactionDto(newWalletId, rubleAmountWithdraw);
        String response = given(requestSpecification)
                .body(transactionDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .asString();
        assertEquals(response, "Операция прошла успешно. Продано 0.0100000000 bitcoin.");
        CryptoWallet wallet = walletStorage.getById(newWalletId).get();
        assertEquals(0, wallet.getBalance().compareTo(new BigDecimal("0.99")));
    }

    @Test
    public void withdrawRubBalanceUnsuccessful() {
        UUID newWalletId = UUID.randomUUID();
        walletStorage.save(new CryptoWallet(newWalletId, USER_LOGIN, BTC, BigDecimal.ZERO));
        BigDecimal rubleAmountWithdraw = new BigDecimal("10000");
        CryptoWalletTransactionDto transactionDto = new CryptoWalletTransactionDto(newWalletId, rubleAmountWithdraw);
        given(requestSpecification)
                .body(transactionDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.BAD_REQUEST.value());
        CryptoWallet wallet = walletStorage.getById(newWalletId).get();
        assertEquals(0, wallet.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void getRubleBalanceWalletSuccess() {
        UUID newWalletId = UUID.randomUUID();
        BigDecimal currentBalanceInBTC = new BigDecimal("10");
        walletStorage.save(new CryptoWallet(newWalletId, USER_LOGIN, BTC, currentBalanceInBTC));
        BigDecimal balanceInRub = given(requestSpecification)
                .pathParam("id", newWalletId)  // добавляем UUID в путь
                .get("/balance/{id}")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(BigDecimal.class);
        assertEquals(0, balanceInRub.compareTo(new BigDecimal("10000000")));
    }

    @Test
    public void getAmountWalletsBalanceInRub() {
        walletStorage.save(new CryptoWallet(UUID.randomUUID(), USER_LOGIN, BTC, new BigDecimal("0.123")));
        walletStorage.save(new CryptoWallet(UUID.randomUUID(), USER_LOGIN, BTC, new BigDecimal("0.1")));
        BigDecimal balanceInRub = given(requestSpecification)
                .queryParams(Map.of("username", USER_LOGIN))  // добавляем UUID в путь
                .get("/balance")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(BigDecimal.class);
        assertEquals(0, balanceInRub.compareTo(new BigDecimal("223000")));
    }
}
