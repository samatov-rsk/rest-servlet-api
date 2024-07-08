import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import samatov.rest.api.dto.UserDTO;
import samatov.rest.api.dto.UserDTOWithOutEvents;
import samatov.rest.api.mapper.UserMapper;
import samatov.rest.api.model.User;
import samatov.rest.api.repository.UserRepository;
import samatov.rest.api.repository.impl.UserRepositoryImpl;
import samatov.rest.api.service.UserService;
import samatov.rest.api.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserServiceTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Когда getAllUser вызывается, то успешный результат")
    void getAllUserTest() {
        List<User> expectedUsers = Arrays.asList(
                new User(1, "User1", Collections.emptyList()),
                new User(2, "User2", Collections.emptyList())
        );

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserDTO> actualUsers = userService.getAllUser();

        assertEquals(expectedUsers.size(), actualUsers.size());

        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEquals(expectedUsers.get(i).getId(), actualUsers.get(i).getId());
            assertEquals(expectedUsers.get(i).getName(), actualUsers.get(i).getName());
        }

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Когда getUserById вызывается, то успешный результат")
    void getUserByIdTest() {
        User expectedUser = new User(1, "User1", null);
        when(userRepository.findById(anyInt())).thenReturn(expectedUser);

        UserDTOWithOutEvents actualUser = userService.getUserById(1);

        assertEquals(expectedUser.getId(), actualUser.getId());
        verify(userRepository).findById(anyInt());
    }

    @Test
    @DisplayName("Когда getUserById вызывается, то NotFoundException")
    void throwExceptionWhenUserNotFound() {
        when(userRepository.findById(anyInt())).thenThrow(new NotFoundException("Ошибка, пользователь с таким id не найден"));

        assertThrows(NotFoundException.class, () -> userService.getUserById(1));
        verify(userRepository).findById(anyInt());
    }

    @Test
    @DisplayName("Когда createUser вызывается, то успешный результат")
    void createUserTest() {
        UserDTOWithOutEvents userToSave = new UserDTOWithOutEvents();
        User savedUser = UserMapper.toUserEntityWithOutEvents(userToSave);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTOWithOutEvents actualUser = userService.createUser(userToSave);

        assertNotNull(actualUser);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Когда createUser вызывается, то UserException")
    void throwExceptionWhenCreateUserFails() {
        UserDTOWithOutEvents userToSave = new UserDTOWithOutEvents();
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Ошибка запроса, пользователя не удалось сохранить..."));

        assertThrows(RuntimeException.class, () -> userService.createUser(userToSave));
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Когда updateUser вызывается, то успешный результат")
    void updateUserTest() {
        UserDTOWithOutEvents userToUpdate = new UserDTOWithOutEvents();
        User updatedUser = UserMapper.toUserEntityWithOutEvents(userToUpdate);
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);

        UserDTOWithOutEvents actualUser = userService.updateUser(userToUpdate);

        assertNotNull(actualUser);
        verify(userRepository).update(any(User.class));
    }

    @Test
    @DisplayName("Когда updateUser вызывается, то UserException")
    void throwExceptionWhenUpdateUserFails() {
        UserDTOWithOutEvents userToUpdate = new UserDTOWithOutEvents();
        when(userRepository.update(any(User.class))).thenThrow(new RuntimeException("Ошибка запроса, пользователя не удалось обновить..."));

        assertThrows(RuntimeException.class, () -> userService.updateUser(userToUpdate));
        verify(userRepository).update(any(User.class));
    }

    @Test
    @DisplayName("Когда deleteUserById вызывается, то успешный результат")
    void deleteUserByIdTest() {
        doNothing().when(userRepository).removeById(anyInt());

        userService.deleteUserById(1);

        verify(userRepository).removeById(anyInt());
    }

    @Test
    @DisplayName("Когда deleteUserById вызывается, то UserException")
    void throwExceptionWhenDeleteUserFails() {
        doThrow(new RuntimeException("Ошибка запроса, пользователя не удалось удалить")).when(userRepository).removeById(anyInt());

        assertThrows(RuntimeException.class, () -> userService.deleteUserById(1));
        verify(userRepository).removeById(anyInt());
    }
}
