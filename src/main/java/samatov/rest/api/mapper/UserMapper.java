package samatov.rest.api.mapper;

import samatov.rest.api.dto.UserDTO;
import samatov.rest.api.dto.UserDTOWithOutEvents;
import samatov.rest.api.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static User toUserEntityWithOutEvents(UserDTOWithOutEvents userDTOWithOutEvents) {
        return User.builder()
                .id(userDTOWithOutEvents.getId())
                .name(userDTOWithOutEvents.getName())
                .build();
    }

    public static UserDTOWithOutEvents toUserDTOWithOutEvents(User user) {
        return UserDTOWithOutEvents.builder()
                .id(user.getId())
                .name(user.getName())
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
