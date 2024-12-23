package com.javaacademy.crypto_wallet.unit;

import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.exception.EntityAlreadyExistException;
import com.javaacademy.crypto_wallet.exception.EntityNotFoundException;
import com.javaacademy.crypto_wallet.storage.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserStorageTest {

    private static final String USER_LOGIN = "denis";
    private static final String USER_EMAIL = "denis@mail.ru";
    private static final String USER_PASSWORD = "111111";

    private User user;

    @BeforeEach
    public void init() {
        user = new User(
                USER_LOGIN,
                USER_EMAIL,
                USER_PASSWORD
        );
    }

    @Test
    public void saveUserSuccess() {
        UserStorage userStorage = new UserStorage();
        assertDoesNotThrow(() -> userStorage.save(user));
    }

    @Test
    public void saveDuplicateLoginUnsuccessful() {
        UserStorage userStorage = new UserStorage();
        userStorage.save(user);
        assertThrows(EntityAlreadyExistException.class, () -> userStorage.save(user));
    }

    @Test
    public void getUserByLoginReturnNull() {
        UserStorage userStorage = new UserStorage();
        assertTrue(userStorage.getByLogin(USER_LOGIN).isEmpty());
    }

    @Test
    public void getUserByLoginSuccess() {
        UserStorage userStorage = new UserStorage();
        userStorage.save(user);
        Optional<User> optionalUser = userStorage.getByLogin(USER_LOGIN);
        assertEquals(user, optionalUser.get());
    }

    @Test
    public void updateUserSuccess() {
        UserStorage userStorage = new UserStorage();
        userStorage.save(user);
        String newPassword = "222222";
        User updatedUser = new User(
                USER_LOGIN,
                USER_EMAIL,
                newPassword
        );
        userStorage.update(updatedUser);
        User userWithNewPassword = userStorage.getByLogin(user.getLogin()).get();
        assertEquals(userWithNewPassword.getPassword(), newPassword);
        assertEquals(userWithNewPassword, userStorage.getByLogin(USER_LOGIN).get());
    }

    @Test
    public void updateNotExistUserUnsuccessful() {
        UserStorage userStorage = new UserStorage();
        assertThrows(EntityNotFoundException.class, () -> userStorage.update(user));
    }
}
