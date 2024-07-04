package samatov.rest.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.UserDTO;
import samatov.rest.api.mapper.UserMapper;
import samatov.rest.api.model.User;
import samatov.rest.api.repository.impl.UserRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepositoryImpl userRepository;

    public List<UserDTO> getAllUser() {
        log.info("Получение всех пользователей");
        List<User> labels = userRepository.findAll();
        log.info("Найдено {} пользователей", labels.size());
        return labels.stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer id) {
        log.info("Получение пользователя по id: {} ", id);
        User user = userRepository.findById(id);
        log.info("Получен пользователь с id: {} ", id);
        return UserMapper.toUserDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        log.info("Добавление нового пользователя: {} ", userDTO);
        User user = UserMapper.toUserEntity(userDTO);
        user = userRepository.save(user);
        log.info("Новый пользователь добавлен: {} ", userDTO);
        return UserMapper.toUserDTO(user);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        log.info("Изменение пользователя: {} ", userDTO);
        User user = UserMapper.toUserEntity(userDTO);
        user = userRepository.update(user);
        log.info("Пользователь изменен: {} ", userDTO);
        return UserMapper.toUserDTO(user);
    }

    public void deleteUserById(Integer id) {
        userRepository.removeById(id);
        log.info("Удаление пользователя по id: {} ", id);
    }
}
