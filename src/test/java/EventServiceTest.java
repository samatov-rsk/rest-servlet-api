import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import samatov.rest.api.dto.EventDTOWithOutUser;
import samatov.rest.api.exception.NotFoundException;
import samatov.rest.api.mapper.EventMapper;
import samatov.rest.api.model.Event;
import samatov.rest.api.repository.EventRepository;
import samatov.rest.api.service.EventService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Когда getAllEvents вызывается, то успешный результат")
    void getAllEventsTest() {
        List<Event> expectedEvents = Arrays.asList(new Event(1, null, null), new Event(2, null, null));
        when(eventRepository.findAll()).thenReturn(expectedEvents);

        List<EventDTOWithOutUser> actualEvents = eventService.getAllEvents();

        assertEquals(expectedEvents.size(), actualEvents.size());
        verify(eventRepository).findAll();
    }

    @Test
    @DisplayName("Когда getEventById вызывается, то успешный результат")
    void getEventByIdTest() {
        Event expectedEvent = new Event(1, null, null);
        when(eventRepository.findById(anyInt())).thenReturn(expectedEvent);

        EventDTOWithOutUser actualEvent = eventService.getEventById(1);

        assertEquals(expectedEvent.getId(), actualEvent.getId());
        verify(eventRepository).findById(anyInt());
    }

    @Test
    @DisplayName("Когда getEventById вызывается, то NotFoundException")
    void throwExceptionWhenEventNotFound() {
        when(eventRepository.findById(anyInt())).thenThrow(new NotFoundException("Ошибка, событие с таким id не найдено"));

        assertThrows(NotFoundException.class, () -> eventService.getEventById(1));
        verify(eventRepository).findById(anyInt());
    }

    @Test
    @DisplayName("Когда createEvent вызывается, то успешный результат")
    void createEventTest() {
        EventDTOWithOutUser eventToSave = new EventDTOWithOutUser();
        Event savedEvent = EventMapper.toEventEntity(eventToSave);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        EventDTOWithOutUser actualEvent = eventService.createEvent(eventToSave);

        assertNotNull(actualEvent);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    @DisplayName("Когда createEvent вызывается, то EventException")
    void throwExceptionWhenCreateEventFails() {
        EventDTOWithOutUser eventToSave = new EventDTOWithOutUser();
        when(eventRepository.save(any(Event.class)))
                .thenThrow(new RuntimeException("Ошибка запроса, событие не удалось сохранить..."));

        assertThrows(RuntimeException.class, () -> eventService.createEvent(eventToSave));
        verify(eventRepository).save(any(Event.class));
    }
}
