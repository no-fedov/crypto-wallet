package com.javaacademy.crypto_wallet.integration;

import com.javaacademy.crypto_wallet.dto.UserCreateDto;
import com.javaacademy.crypto_wallet.dto.UserDto;
import com.javaacademy.crypto_wallet.dto.UserUpdatePasswordDto;
import com.javaacademy.crypto_wallet.entity.User;
import com.javaacademy.crypto_wallet.exception.EntityAlreadyExistException;
import com.javaacademy.crypto_wallet.exception.EntityNotFoundException;
import com.javaacademy.crypto_wallet.exception.InvalidPasswordException;
import com.javaacademy.crypto_wallet.mapper.UserMapper;
import com.javaacademy.crypto_wallet.repository.UserRepository;
import com.javaacademy.crypto_wallet.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("local")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceImpTest {

    private static final String USER_LOGIN = "login";
    private static final String USER_EMAIL = "email";
    private static final String USER_OLD_PASSWORD = "password";
    private static final String USER_NEW_PASSWORD = "updated_password";

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @SpyBean
    private UserRepository userRepository;

    @Test
    public void saveSuccess() {
        UserCreateDto userCreateDto = new UserCreateDto(
                USER_LOGIN,
                USER_EMAIL,
                USER_OLD_PASSWORD
        );
        User expectedSavedUser = new User(
                USER_LOGIN,
                USER_EMAIL,
                USER_OLD_PASSWORD
        );
        UserDto expectedUserDto = new UserDto(USER_LOGIN);
        userService.save(userCreateDto);
        verify(userRepository, times(1)).save(expectedSavedUser);
        UserDto userDto = userService.findByLogin(USER_LOGIN);
        assertEquals(expectedUserDto, userDto);
        verify(userRepository, times(1)).getByLogin(USER_LOGIN);
    }

    @Test
    public void saveDuplicateUnsuccessful() {
        UserCreateDto userCreateDto = new UserCreateDto(
                USER_LOGIN,
                USER_EMAIL,
                USER_OLD_PASSWORD
        );
        User expectedSavedUser = new User(
                USER_LOGIN,
                USER_EMAIL,
                USER_OLD_PASSWORD
        );
        userService.save(userCreateDto);
        assertThrows(EntityAlreadyExistException.class, () -> userService.save(userCreateDto));
        verify(userRepository, times(2)).save(expectedSavedUser);
    }

    @Test
    public void findNonExistentUserUnsuccessful() {
        assertThrows(EntityNotFoundException.class, () -> userService.findByLogin(USER_LOGIN));
    }

    @Test
    public void findSuccess() {
        UserCreateDto userCreateDto = new UserCreateDto(
                USER_LOGIN,
                USER_EMAIL,
                USER_OLD_PASSWORD
        );
        UserDto expectedUserDto = new UserDto(USER_LOGIN);
        userService.save(userCreateDto);
        UserDto userDto = userService.findByLogin(USER_LOGIN);
        assertEquals(expectedUserDto, userDto);
    }

    @Test
    public void resetPasswordForNonExistentUserUnsuccessful() {
        UserUpdatePasswordDto dto = new UserUpdatePasswordDto(
                USER_LOGIN,
                USER_OLD_PASSWORD,
                USER_NEW_PASSWORD
        );
        assertThrows(EntityNotFoundException.class, () -> userService.resetPassword(dto));
    }

    @Test
    public void resetPasswordSuccess() {
        UserCreateDto userCreateDto = new UserCreateDto(
                USER_LOGIN,
                USER_EMAIL,
                USER_OLD_PASSWORD
        );
        UserUpdatePasswordDto userUpdatePasswordDto = new UserUpdatePasswordDto(
                USER_LOGIN,
                USER_OLD_PASSWORD,
                USER_NEW_PASSWORD
        );
        User updatedUser = new User(
                USER_LOGIN,
                USER_EMAIL,
                USER_NEW_PASSWORD
        );

        userService.save(userCreateDto);
        userService.resetPassword(userUpdatePasswordDto);
        User user = userRepository.getByLogin(USER_LOGIN).get();
        assertEquals(updatedUser, user);
    }

    @Test
    public void resetPasswordUnsuccessful() {
        UserCreateDto userCreateDto = new UserCreateDto(
                USER_LOGIN,
                USER_EMAIL,
                USER_OLD_PASSWORD
        );
        UserUpdatePasswordDto userUpdatePasswordDto = new UserUpdatePasswordDto(
                USER_LOGIN,
                USER_NEW_PASSWORD,
                USER_NEW_PASSWORD
        );
        userService.save(userCreateDto);
        assertThrows(InvalidPasswordException.class, () -> userService.resetPassword(userUpdatePasswordDto));
    }
}
