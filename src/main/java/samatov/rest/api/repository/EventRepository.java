package samatov.rest.api.repository;

import samatov.rest.api.model.Event;

import java.util.List;

public interface EventRepository <T, ID> {

    List<Event> findAll();
    Event findById(Integer id);
    Event save(Event event);
}
