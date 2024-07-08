package samatov.rest.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.UserDTO;
import samatov.rest.api.dto.UserDTOWithOutEvents;
import samatov.rest.api.mapper.UserMapper;
import samatov.rest.api.model.User;
import samatov.rest.api.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> getAllUser() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAll();
        log.info("Найдено {} пользователей", users.size());
        return users.stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTOWithOutEvents getUserById(Integer id) {
        log.info("Получение пользователя по id: {} ", id);
        User user = userRepository.findById(id);
        log.info("Получен пользователь с id: {} ", id);
        return UserMapper.toUserDTOWithOutEvents(user);
    }

    public UserDTOWithOutEvents createUser(UserDTOWithOutEvents userDTO) {
        log.info("Добавление нового пользователя: {} ", userDTO);
        User user = UserMapper.toUserEntityWithOutEvents(userDTO);
        user = userRepository.save(user);
        log.info("Новый пользователь добавлен: {} ", userDTO);
        return UserMapper.toUserDTOWithOutEvents(user);
    }

    public UserDTOWithOutEvents updateUser(UserDTOWithOutEvents userDTO) {
        log.info("Изменение пользователя: {} ", userDTO);
        User user = UserMapper.toUserEntityWithOutEvents(userDTO);
        user = userRepository.update(user);
        log.info("Пользователь изменен: {} ", userDTO);
        return UserMapper.toUserDTOWithOutEvents(user);
    }

    public void deleteUserById(Integer id) {
        userRepository.removeById(id);
        log.info("Удаление пользователя по id: {} ", id);
    }
}
