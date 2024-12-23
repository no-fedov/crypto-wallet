package com.javaacademy.crypto_wallet.unit;

import com.javaacademy.crypto_wallet.dto.UserCreateDto;
import com.javaacademy.crypto_wallet.dto.UserDto;
import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Test
    public void convertToUserFromUser() {
        UserMapper userMapper = new UserMapper();
        User user = new User(
                "login",
                "email",
                "password"
        );
        UserDto expectedUserDto = new UserDto("login");
        UserDto userDto = userMapper.convertToUserDto(user);
        assertEquals(expectedUserDto, userDto);
    }

    @Test
    public void convertToUserFromUserCreateDto() {
        UserMapper userMapper = new UserMapper();
        UserCreateDto userCreateDto = new UserCreateDto(
                "login",
                "email",
                "password"
        );
        User expectedUser = new User(
                "login",
                "email",
                "password"
        );
        User convertedToUser = userMapper.convertToUser(userCreateDto);
        assertEquals(expectedUser, convertedToUser);
    }
}
