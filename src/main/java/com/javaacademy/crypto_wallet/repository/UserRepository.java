package com.javaacademy.crypto_wallet.repository;

import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.mapper.UserMapper;
import com.javaacademy.crypto_wallet.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepository {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public Optional<User> getByLogin(String login) {
        log.info("Search user by login = {}", login);
        return userStorage.getByLogin(login);
    }

    public void save(User user) {
        userStorage.save(user);
        log.info("User with login = {} is saved", user.getLogin());
    }

    public void update(User user) {
        log.info("User with login = {} has been updated", userMapper.convertToUserDto(user));
        userStorage.update(user);
    }

}
