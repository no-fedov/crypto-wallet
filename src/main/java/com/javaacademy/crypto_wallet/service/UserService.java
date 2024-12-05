package com.javaacademy.crypto_wallet.service;

import com.javaacademy.crypto_wallet.dto.UserCreateDto;
import com.javaacademy.crypto_wallet.dto.UserDto;
import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.mapper.UserMapper;
import com.javaacademy.crypto_wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void save(UserCreateDto userCreateDto) {
        User newUser = userMapper.convertToUser(userCreateDto);
        userRepository.save(newUser);
    }

    public UserDto findByLogin(String login) {
        Optional<User> foundUser = userRepository.getByLogin(login);
        if (foundUser.isEmpty()) {
            throw new RuntimeException("User with login = %s not found".formatted(login));
        }
        return userMapper.convertToUserDto(foundUser.get());
    }
}
