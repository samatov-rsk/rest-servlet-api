package samatov.rest.api.mapper;

import samatov.rest.api.dto.UserDTO;
import samatov.rest.api.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static User toUserEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .events(userDTO.getEvents().stream()
                        .map(EventMapper::toEventEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .events(user.getEvents().stream()
                        .map(EventMapper::toEventDto)
                        .collect(Collectors.toList()))
                .build();
    }
}