package com.javaacademy.crypto_wallet.service;

import com.javaacademy.crypto_wallet.dto.UserCreateDto;
import com.javaacademy.crypto_wallet.dto.UserDto;
import com.javaacademy.crypto_wallet.dto.UserUpdatePasswordDto;

public interface UserService {

    void save(UserCreateDto dto);

    UserDto findByLogin(String login);

    void resetPassword(UserUpdatePasswordDto dto);

}
