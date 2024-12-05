package com.javaacademy.crypto_wallet.repository;

import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepository {
    private final UserStorage userStorage;

    public Optional<User> getByLogin(String login) {
        return userStorage.getByLogin(login);
    }

    public void save(User user) {
        userStorage.save(user);
    }
}
