package samatov.rest.api.mapper;

import samatov.rest.api.dto.EventDTO;
import samatov.rest.api.model.Event;

public class EventMapper {

    public static Event toEventEntity(EventDTO eventDTO) {
        return Event.builder()
                .id(eventDTO.getId())
                .user(UserMapper.toUserEntity(eventDTO.getUser()))
                .file(FileMapper.toFileEntity(eventDTO.getFile()))
                .build();
    }

    public static EventDTO toEventDto(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .user(UserMapper.toUserDTO(event.getUser()))
                .file(FileMapper.toFileDto(event.getFile()))
                .build();
    }

}
