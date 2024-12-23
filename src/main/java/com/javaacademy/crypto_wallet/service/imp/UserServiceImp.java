package com.javaacademy.crypto_wallet.service.imp;

import com.javaacademy.crypto_wallet.dto.UserCreateDto;
import com.javaacademy.crypto_wallet.dto.UserDto;
import com.javaacademy.crypto_wallet.dto.UserUpdatePasswordDto;
import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.exception.EntityNotFoundException;
import com.javaacademy.crypto_wallet.exception.InvalidPasswordException;
import com.javaacademy.crypto_wallet.mapper.UserMapper;
import com.javaacademy.crypto_wallet.repository.UserRepository;
import com.javaacademy.crypto_wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void save(UserCreateDto dto) {
        User newUser = userMapper.convertToUser(dto);
        userRepository.save(newUser);
        log.info("User with login = {} is saved", dto.getLogin());
    }

    @Override
    public UserDto findByLogin(String login) {
        User foundUser = findUserByLogin(login);
        log.info("User has been found, login = {}", login);
        return userMapper.convertToUserDto(foundUser);
    }

    @Override
    public void resetPassword(UserUpdatePasswordDto dto) {
        User currentUser = findUserByLogin(dto.getLogin());
        if (!Objects.equals(currentUser.getPassword(), dto.getOldPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }
        currentUser.setPassword(dto.getNewPassword());
        userRepository.update(currentUser);
        UserDto updatedUserDto = userMapper.convertToUserDto(currentUser);
        log.info("User {} update password", updatedUserDto);
    }

    private User findUserByLogin(String login) {
        Optional<User> foundUser = userRepository.getByLogin(login);
        if (foundUser.isEmpty()) {
            log.warn("User login = {} was not found", login);
            throw new EntityNotFoundException("User with login = %s not found".formatted(login));
        }
        return foundUser.get();
    }
}
