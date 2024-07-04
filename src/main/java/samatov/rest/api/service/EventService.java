package samatov.rest.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.EventDTO;
import samatov.rest.api.mapper.EventMapper;
import samatov.rest.api.model.Event;
import samatov.rest.api.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;

    public List<EventDTO> getAllEvents() {
        log.info("Получение всех событий");
        List<Event> events = eventRepository.findAll();
        log.info("Найдено {} событий", events.size());
        return events.stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(Integer id) {
        log.info("Получение события по id: {}", id);
        Event event = eventRepository.findById(id);
        log.info("Получено событие с id: {}", id);
        return EventMapper.toEventDto(event);
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        log.info("Добавление нового события: {}", eventDTO);
        Event event = EventMapper.toEventEntity(eventDTO);
        event = eventRepository.save(event);
        log.info("Новое событие добавлено: {}", eventDTO);
        return EventMapper.toEventDto(event);
    }
}
