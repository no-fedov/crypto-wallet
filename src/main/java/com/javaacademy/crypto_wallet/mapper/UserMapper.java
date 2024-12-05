package com.javaacademy.crypto_wallet.mapper;

import com.javaacademy.crypto_wallet.dto.UserCreateDto;
import com.javaacademy.crypto_wallet.dto.UserDto;
import com.javaacademy.crypto_wallet.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto convertToUserDto(User user) {
        return new UserDto(user.getLogin());
    }

    public User convertToUser(UserCreateDto userCreateDto) {
        return new User(
                userCreateDto.getLogin(),
                userCreateDto.getEmail(),
                userCreateDto.getPassword()
        );
    }
}
