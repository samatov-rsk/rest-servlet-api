package samatov.rest.api.mapper;

import samatov.rest.api.dto.EventDTOWithOutUser;
import samatov.rest.api.model.Event;

public class EventMapper {

    public static Event toEventEntity(EventDTOWithOutUser eventDTO) {
        return Event.builder()
                .id(eventDTO.getId())
                .user(UserMapper.toUserEntityWithOutEvents(eventDTO.getUser()))
                .file(FileMapper.toFileEntity(eventDTO.getFile()))
                .build();
    }

    public static EventDTOWithOutUser toEventDto(Event event) {
        return EventDTOWithOutUser.builder()
                .id(event.getId())
                .user(UserMapper.toUserDTOWithOutEvents(event.getUser()))
                .file(FileMapper.toFileDto(event.getFile()))
                .build();
    }
}
