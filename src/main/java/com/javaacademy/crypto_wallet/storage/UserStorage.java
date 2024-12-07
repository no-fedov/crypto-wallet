package com.javaacademy.crypto_wallet.storage;

import com.javaacademy.crypto_wallet.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserStorage {
    private final Map<String, User> storage = new HashMap<>();

    public Optional<User> getByLogin(String login) {
        return Optional.ofNullable(storage.get(login));
    }

    public void save(User user) {
        String login = user.getLogin();
        if (storage.containsKey(login)) {
            throw new RuntimeException("Login '%s' already exists".formatted(login));
        }
        storage.put(login, user);
    }

    public User update(User user) {
        return storage.put(user.getLogin(), user);
    }
}
