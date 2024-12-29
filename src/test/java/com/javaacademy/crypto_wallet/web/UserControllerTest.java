package com.javaacademy.crypto_wallet.web;

import com.javaacademy.crypto_wallet.dto.UserCreateDto;
import com.javaacademy.crypto_wallet.dto.UserUpdatePasswordDto;
import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.storage.UserStorage;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("local")
public class UserControllerTest {

    @Autowired
    private UserStorage userStorage;

    private static final String USER_LOGIN = "den4ik";
    private static final String USER_EMAIL = "email@mail.ru";
    private static final String USER_PASSWORD_OLD = "111111";
    private static final String USER_PASSWORD_NEW = "222222";

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setPort(9090)
            .setBasePath("/user")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    @Test
    public void saveUserSuccess() {
        UserCreateDto userCreateDto = new UserCreateDto(USER_LOGIN, USER_EMAIL, USER_PASSWORD_OLD);
        given(requestSpecification)
                .body(userCreateDto)
                .post("/signup")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value());

        User savedUser = userStorage.getByLogin(USER_LOGIN).get();
        assertEquals(userCreateDto.getLogin(), savedUser.getLogin());
        assertEquals(userCreateDto.getEmail(), savedUser.getEmail());
        assertEquals(userCreateDto.getPassword(), savedUser.getPassword());
    }

    @Test
    public void saveUserWithDuplicateLoginUnsuccessful() {
        UserCreateDto userCreateDto = new UserCreateDto(USER_LOGIN, USER_EMAIL, USER_PASSWORD_OLD);
        userStorage.save(new User(USER_LOGIN, USER_LOGIN, USER_PASSWORD_OLD));
        given(requestSpecification)
                .body(userCreateDto)
                .post("/signup")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void resetPasswordSuccess() {
        userStorage.save(new User(USER_LOGIN, USER_EMAIL, USER_PASSWORD_OLD));
        UserUpdatePasswordDto userCreateDto = new UserUpdatePasswordDto(USER_LOGIN, USER_PASSWORD_OLD, USER_PASSWORD_NEW);
        given(requestSpecification)
                .body(userCreateDto)
                .post("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value());
        User updatedUser = userStorage.getByLogin(USER_LOGIN).get();
        assertEquals(USER_PASSWORD_NEW, updatedUser.getPassword());
        assertEquals(USER_LOGIN, updatedUser.getLogin());
        assertEquals(USER_EMAIL, updatedUser.getEmail());
    }

    @Test
    public void resetPasswordNotFoundUserUnsuccessful() {
        UserUpdatePasswordDto userCreateDto = new UserUpdatePasswordDto(USER_LOGIN, USER_PASSWORD_OLD, USER_PASSWORD_NEW);
        given(requestSpecification)
                .body(userCreateDto)
                .post("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("code", equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void resetPasswordInvalidEnteredPasswordUnsuccessful() {
        userStorage.save(new User(USER_LOGIN, USER_EMAIL, USER_PASSWORD_OLD));
        UserUpdatePasswordDto userCreateDto = new UserUpdatePasswordDto(USER_LOGIN, USER_PASSWORD_NEW, USER_PASSWORD_NEW);
        given(requestSpecification)
                .body(userCreateDto)
                .post("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", equalTo(HttpStatus.BAD_REQUEST.value()));
        User user = userStorage.getByLogin(USER_LOGIN).get();
        assertEquals(USER_PASSWORD_OLD, user.getPassword());
    }
}
